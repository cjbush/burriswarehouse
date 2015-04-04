# Auto Completion. Due: 2/28 #

Here is how I'm envisioning this working from the user's perspective: I the user will need to initiate the function by pushing a key. Not sure which key...it doesn't really matter. This will then call a function which will start something like a cut-scene: a short movie which will start at the beginning of the pic-job and walk through each segment of it. The short clip will then stop, and the screen will then show the player in the original location, ready to go.

Just to be clear, Chris is the main person on this. I'm going to be helping as much as I can, but he seems to be a bit better qualified for this one specifically.


## Details ##

-pre-req: the 2d bounding boxes.

-Details:
  * Putting the way points into a db, or getting them from the vocollect db already in existence. (1 day, done by 2/17)
  * Making the player walk there (sort of like the Npc system...). (1 day, done by 2/21)
  * Cleaning up the beginning and ending so that it fades, or is somehow clear that the user no longer has control. (3-4 days, done by 2/26).



# "Collision detection" using grid. Due: 2/14. #

Basic idea: we're going to make custom "bounding boxes". Basically, what this means is that we're going to surround everything that is going to be "non-walkable" -- the majority of the things in this list will be the racks. In fact, whole shelves of racks and the random pallets laying around may be the only things in this list. The actual "collidables" list should dwindle down to things like: the warehouse walls, and the characters, etc...nothing like all of the racks. That can just be done with some 2d stuff.

## Details ##
-pre-requisites: none.

The way I see it, this is sort of a complex problem involving a few steps

  * create a class which contains 4 fields (at least): two X-positions and two Y-positions. This will become the base class for the "non-walkable" sections of the warehouse. (few hours, done by 2/8)
  * create a table in the db that has these 4 fields in the db. This will contain the actual numbers. These numbers will make a rectangle, inside of which, the user will not be able to walk. (few hours, done by 2/10)
  * place these rectangles around large areas (whole rows of racks, for example). This will keep the checking in a 2d field, and not in 3d. Should be able to keep the player out of this box, but simply checking to see if the player wants to walk into it, or if that player is in it, then stopping the player's velocity. Basically. (2-3 days, done by 2/12).
  * the only thing to add is the boxes that the player can't go through. There are probably 10-15 more areas that the player needs to be restricted from. This should only take a couple of hours. (2 hours, done by 2/16).