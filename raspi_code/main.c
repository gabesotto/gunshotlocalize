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

int main(int argc, char **argv)
{
	if (argc < 2)
	{
		fprintf(stderr, "Usage: ./client <ip addr> <port> <GPS path>\n");
		return 1;
	}

	if (argc > 2) port = atoi(argv[2]);

	return 0;
}

