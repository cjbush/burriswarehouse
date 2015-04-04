# Critical Tasks #

## Collision Detection ##

Because our character only moves in two directions, we only need to detect collisions in those two dimensions. By eliminating the third dimension, it will greatly reduce the amount of time needed to calculate collisions. We can also reduce the number of calculations needed by surrounding entire sections (e.g. an entire row of racks) with a 2D bounding box, rather than each individual rack. This task belongs to both Caleb and me.

### Subtasks ###

  * Create a 2D box object, similar to a bounding box but in two dimensions instead of 3. (2/9)
  * Modify the application's database to add a table with XY coordinates to specify size and location of all the necessary boxes. (2/10)
  * Write a function to detect collisions between the players 2D bounding box and the 2D bounding boxes loaded from the database. (2/12)
  * Test and make adjustments. (2/14)



## Auto Completion ##
Rather than continuing on from where the user currently is, it will be substantially easier and provide more functionality to essentially implement auto-completion as a cut scene. Essentially, the user will hit a button and the player will be reset to the starting position, begin walking on a pre-recorded path along the pick route, and then returned to where the player was standing after the auto-pick-job is complete.

### Subtasks ###

  * Implement function to reset player to beginning (2/15)
  * Implement database table similar to NPC table that uses a waypoint system for each pick job (2/17)
  * Add a boolean flag to the player class to tell it whether to be controlled by the player or by the waypoint system (2/17)
  * Add functionality similar to the NPC class to go to each waypoint (2/19)
  * Testing (2/21)


# Other Tasks #

## Fetch Product Textures ##
I'll fetch images from Google to texture the boxes in the warehouse with pertinent labels (yogurt labels in the dairy section, etc). This will involve parsing an XML document from Burris with all the relevant information and forming a query from Google that will return the top 10 images.

  * Convert Burris' XLS file into an XML file (done)
  * Write a PHP script to parse the XML (2/9)
  * Add to the script to form a URL to query Google Images (2/10)
  * Add the ability to save the top 10 results to the filesystem for review later. We can then look through them and pick the best images for texturing. (2/14)
  * Write a script to automatically resize all selected pictures to a reasonable size for texturing (2/18)


## Implement SQLite for DB Localization ##
In order to make the app truly standalone (except for the Vocollect), it would be good to package some of the data (such as model loading information, NPC waypoint information, etc.) in SQLite rather than MySQL. This will allow faster access time and won't require a client-server setup for the application.

  * Convert current DB over to SQLite DB file. (2/20)
  * Install SQLite driver for Java. (2/24)
  * Modify model loader to read from SQLite. (2/28)