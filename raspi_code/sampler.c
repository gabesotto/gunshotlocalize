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
#include <math.h>
#include "sampler.h"

static snd_pcm_t *cap_handle;
static snd_pcm_hw_params_t *hw_params;

// NOTE: Remember, sample rate is in Hz. 44,100 Hz is actually a good default for modern
// microphones.
unsigned int sample_rate = 44100; // Not static as it may be needed in another module.

static bool is_running = true;


// Helper function.
static void setup_mic();



// If this is called, we're getting ready to shutdown the program. Close up anything in this module.
void stopListening()
{
	is_running = false;
}

void listenForGunshots(void *callback)
{
	int frames_read;
	int fps; // frames per sample.
	int16_t *buffer;

	// Determine the required buffer size, then allocate it on the heap.
	fps = (int)((float)sample_rate * REC_INT);
	buffer = malloc(sizeof(int16_t) * fps);
	memset(buffer, 0, fps*sizeof(int16_t));

	// Alright ALSA, do your magic.
	setup_mic();

	while (is_running)
	{
		snd_pcm_prepare(cap_handle);
		frames_read = snd_pcm_readi(cap_handle, buffer, fps);
		// FIXME: CONFIRM THAT FRAMES_READ = FPS! WE DON'T WANT AN OVERRUN!!!!

		// Now let's check for a sudden loud noise. We do this if their exist a difference in
		// sample values that differ by as much of 20% of the microphones range. A simple algorithm,
		// but provides a good starting place for us.
		for (int i = 1; i < fps; ++i)
		{
			int16_t last_sample = buffer[i - 1];
			int diff = abs(buffer[i] - last_sample);
	
			// Below threshold? Pffft, next sample!
			if (diff < MIC_THRESH) continue;
	
			// Eeep! Gunshots! Better call the callback, he'll know what to do!
			(*((void (*)(void))callback))();
	
			break; // No need to check the remaining samples. Break the loop.
		}
	}

	// We're done here people. Clean up, and get outa my house.
	free(buffer);
	snd_pcm_close(cap_handle);
	
}

static void setup_mic()
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

	//snd_pcm_prepare(cap_handle);
}

// Testing function. Not really used anymore, kept around as a reference.
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
