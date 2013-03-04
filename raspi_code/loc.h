/* loc.h
 * Date: 03/04/2013
 * Author: Erik E. Kahn
 *
 * Defines functions for finding the current position of the raspi.
 *
 */

typedef struct
{
	double lat, lon;
} gps_coord;

void init_gps(char *path);
bool getCoordinate(gps_coord *result);
void cleanup_gps();

