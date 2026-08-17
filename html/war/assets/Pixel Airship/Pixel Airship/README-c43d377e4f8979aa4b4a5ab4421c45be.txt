Pixel Airship
==============

Tactical sky adventure asset pack by Masalimov Ilnur.

This pack contains animated pixel-art airships, crew characters, ship weapons, effects, props, drones, doors, ship systems, UI elements, and sky backgrounds for 2D games.

Good for:
- Tactical RPGs / strategy games
- Sky battles and airship adventures
- Roguelite / survival prototypes
- Base, crew, and fleet management games
- Fantasy / steampunk projects
- Game jams and prototypes


Folder Structure
----------------

01_Airships/
  Animated_Airships/
    9 animated airship sprite sheets.

02_Characters/
  Cyclops/
  Demons/
  Dwarves/
  Humans/
  Orcs/
  Skeletons/
    28 character sprite sheets in total.
    6 races: Humans, Orcs, Skeletons, Demons, Dwarves, Cyclops.
    See 02_Characters/README_Animations.txt for animation row order.

03_Ship_Weapons/
  Ship_Cannons_8_types_5frames_48x48.png
    8 animated mountable ship cannons.
    Each cannon has 5 frames.
    Cell size: 48x48 px.

04_Effects/
  Fire/
    Fire animation.
  Healing/
    Healing animation.

05_Props/
  Ship_Props.png
    Ship/deck props such as crates, barrels, bags, anchor, ship wheel, cannonballs, ladders, and other small equipment.

06_Drones/
  Drone_4frames_Turret.png
    Animated drone with 4 frames and a separate turret element.
    Can be used for repair, defense, or attack mechanics.

07_Doors/
  Ship_Doors_Wood_Metal.png
    Wooden and metal doors for ship rooms, boarding mechanics, fire control, or repair gameplay.

08_Ship_Systems/
  Static_Modules/
    Telescope_Control_Medical_Modules_32x32.png
      3 static 32x32 modules: telescope, control module, medical module.

  Animated_Modules/
    Energy_Emitter.png
    Mind_Control_Module.png
    Portal_Gate.png
    System_Console.png
      Animated ship system modules for tactical ship interiors.

09_UI/
  Icons/
    UI_Icons_Black.png
    UI_Icons_Red.png
      Small tactical/system UI icons in two color variants.

  Ship_Interface/
    Ship_Interface_UI_Elements.png
      Modular ship interface elements: bars, buttons, headers, panels, and list panels.

  Tactical_UI/
    Tactical_UI_Elements.png
      Tactical/battle UI elements: main bar, indicators, icons, cards, panels, and connectors.

10_Backgrounds/
  BG.png
  Cloud.png
    Sky background and cloud elements.


Airship Notes
-------------

Airship sprite sheets use a square-style layout where possible.
Frames are read from left to right, starting from the top-left corner, then continue row by row downward.

Important:
- Each airship may have its own frame size and sheet size.
- Do not assume the same cell size for every airship.
- AirShip6 uses a shorter 2-frame sheet because this ship does not have propeller animation like the other ships.


Character Notes
---------------

Characters are designed as 32x32 sprites.
Character sprite sheets include additional spacing / layout padding for easier reading and organization.
Because of this, the PNG sheet size is larger than a strict no-padding 32x32 grid.

Each character has the same animation row order:
01 Idle — 4 frames
02 Walk — 6 frames
03 Ladder_Climb — 2 frames
04 Dead — 5 frames
05 Idle_Armed — 4 frames
06 Walk_Armed — 6 frames
07 Attack — 6 frames
08 Repair — 4 frames
09 Jump — 6 frames
10 Work — 4 frames

Some animations have fewer than 6 frames, so empty spacing may appear at the end of shorter animation rows.


File Format
-----------

- PNG files
- Transparent backgrounds where applicable
- Pixel art sprites and sprite sheets
- Suitable for Unity, Godot, GameMaker, Construct, and other 2D engines


License
-------

See License.txt for the license terms.


Author
------

Masalimov Ilnur
itch.io: https://masalimov-ilnur.itch.io/

Thank you for supporting indie pixel art assets!
