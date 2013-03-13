/* main.c
 * Date: 02/22/2013
 * Author: Erik E. Kahn
 * 
 * This is the main file for the rasberry pi gunshot client codebase. This 
 * mainly continously listens for a sound with the signature of a gunshot (for
 * our purposes, this is a noise that is significantly louder than anything we've
 * heard within some small time period). 
 *
 * Usage:
 * ./client [-D]
 * 
 * Using the -D flag enters debug mode.
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <pthread.h>
#include "loc.h"
#include "sampler.h"

#define DEBUG_MSG(x) (printf("DEBUG: " x))

//static unsigned short port;
static char *config_file = "./CONFIG";

/* This will actually be global, and used for debugging. */
bool debug = false;

// This is a function that should be called from the gunshot listener. It is
// only called upon the detection of a gunshot.
void gunshot_handler()
{
	// TODO: RESUME HERE
	// Need to send results to chris. Make sure we have GPS coords. Don't forget
	// to sleep for ~5 seconds. We don't want to pick up the same gunshot noise.
}

void load_config()
{
	static char config_key[64],config_value[64];
	FILE *f = fopen(config_file, "r");

	if (!f)
	{
		fprintf(stderr, "Error: Couldn't find config file '%s'. Exiting...\n", config_file);
		exit(1);
	}

	while ( fscanf(f, "%s = %s\n", config_key, config_value) == 2)
	{
		printf("%s is %s\n", config_key, config_value);
	}



	fclose(f);
}

int main(int argc, char **argv)
{
	pthread_t thread;


	// Some good oll command line handling code. Ahhh, don't you just love it?
	for (int i = 1; i < argc; ++i)
	{
		if (argv[i][0] == '-')
		{
			char flag = argv[i][1];
			switch (flag)
			{
				case 'D':
					DEBUG_MSG("Debug mode active\n");
					debug = true;
					break;
				default:
					printf("Unknown flag '-%c', ignoring...\n", flag);
					break;
			}
		}
		else
			printf("Garbage input '%s', ignoring....\n", argv[i]);
	}
	putchar('\n');

	// And load the configuration file, or we aren't going to be doing much!
	load_config();


	// No time to waste. Get cracking on listening to that gunshot.
	pthread_create(&thread, NULL, &listenForGunshots, &gunshot_handler);

	while (true)
	{
		// This code will be responsible for updating the GPS location. For now,
		// just let the program keep running. The other thread will do the necesary
		// magic tricks.
		sleep(1);
	}

	return 0;
}

