/* sampler.c
 * Date: 03/04/2013
 * Author: Erik E. Kahn
 *
 * Implements functions for dealing with microphone, and performs testing of samples
 * to determine if a gunshot has in fact occoured.
 */

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <alsa/asoundlib.h>
#include "sampler.h"

static snd_pcm_t *cap_handle;
static snd_pcm_hw_params_t *hw_params;

unsigned int sample_rate = 44100; // Not static as it may be needed in another module.

void setup_mic()
{
	//TODO: Add error handling, becuase god knows something will eventually go wrong....
	snd_pcm_open(&cap_handle, "plughw:0,0", SND_PCM_STREAM_CAPTURE, 0);
	snd_pcm_hw_params_malloc(&hw_params);

	snd_pcm_hw_params_any(cap_handle, hw_params);
	snd_pcm_hw_params_set_access(cap_handle, hw_params, SND_PCM_ACCESS_RW_INTERLEAVED);   
	snd_pcm_hw_params_set_format(cap_handle, hw_params, SND_PCM_FORMAT_S16_LE); /* Signed 16-bit little endian */
	snd_pcm_hw_params_set_rate_near(cap_handle, hw_params, &sample_rate, 0);
	snd_pcm_hw_params_set_channels(cap_handle, hw_params, 1); /* Mono channel. No need for 2, silly. */
	snd_pcm_hw_params(cap_handle, hw_params); /* Apply our settings */

	snd_pcm_hw_params_free(hw_params);

	snd_pcm_prepare(cap_handle);
}

void test_func()
{
	static int16_t buffer[1024]; // Samples are 16-bit signed integers :D 

	memset(buffer,(int16_t)7,1024*sizeof(int16_t));
	setup_mic();

	/* NOTE: This function returns the number of frames actually read. Additionally,
	 * should ALSA experience an overrun, it will return a number below 0. must call
	 * the pcm_prepare function to clear out the buffer in order to continue recording
	 * */

	int read_count = snd_pcm_readi(cap_handle, buffer, 1024);

	for (int i = 0; i < 128; ++i)
		printf("%d, ", buffer[i]);
	printf("\nI read %d frames.\n", read_count);

	snd_pcm_close(cap_handle);
}

void cleanup_mic()
{
	snd_pcm_close(cap_handle);
}
