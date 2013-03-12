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
 * ./client <ip addr> <port> <GPS path>
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include "loc.h"
#include "sampler.h"

static unsigned short port;
static char *config_file = "./CONFIG";

/* This will actually be global, and used for debugging. */
bool debug = false;

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
		printf("%s is %s\n", config_key, config_value);



	fclose(f);
}

int main(int argc, char **argv)
{
	load_config();

	return 0;
}

