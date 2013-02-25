/* main.c
 * Date: 02/22/2013
 * Author: Erik E. Kahn
 * 
 * This is the main file for the rasberry pi gunshot client codebase. This 
 * mainly continously listens for a sound with the signature of a gunshot (for
 * our purposes, this is a noise that is significantly louder than anything we've
 * heard within some small time period). 
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <alsa/asoundlib.h> /* ALSA for reading from mic. w00t. */

static snd_pcm_t *cap_handle;
static snd_pcm_hw_params_t *hw_params;

void setup_mic()
{
	//TODO: Add error handling, becuase god knows something will eventually go wrong....
	static unsigned int sample_rate = 44100;
	snd_pcm_open(&cap_handle, "plughw:0,0", SND_PCM_STREAM_CAPTURE, 0);
	snd_pcm_hw_params_malloc(&hw_params);

	snd_pcm_hw_params_any(cap_handle, hw_params);
	snd_pcm_hw_params_set_access(cap_handle, hw_params, SND_PCM_ACCESS_RW_INTERLEAVED); 
	/* Other PCM formats exist. Could use floating point (-1,1) w/ 
    * SND_PCM_FORMAT_FLOAT_LE. You know, just an idea.
    */
	snd_pcm_hw_params_set_format(cap_handle, hw_params, SND_PCM_FORMAT_S16_LE); /* Signed 16-bit little endian */
	snd_pcm_hw_params_set_rate_near(cap_handle, hw_params, &sample_rate, 0);
	snd_pcm_hw_params_set_channels(cap_handle, hw_params, 1); /* Mono channel. No need for 2, silly. */
	snd_pcm_hw_params(cap_handle, hw_params); /* Apply our settings */

	snd_pcm_hw_params_free(hw_params);

	snd_pcm_prepare(cap_handle);
}

int main(int argc, char **argv)
{
	static int16_t buffer[1024]; // Samples are 16-bit signed integers :D

	memset(buffer,(int16_t)7,1024*sizeof(int16_t));
	setup_mic();

	int read_count = snd_pcm_readi(cap_handle, buffer, 1024);

	for (int i = 0; i < 128; ++i)
		printf("%d, ", buffer[i]);
	printf("\nI read %d frames.\n", read_count);
	
	snd_pcm_close(cap_handle);
	return 0;
}

