A robot manipulates containers in a storage room.

The containers can be stacked on top of each other and each container has a name.

The robot can receive commands like:
  - move b1 b2 - which means move the container named b1 on top of the container named b2
  - fill b 50 - which means fill container b with 50l of fluid.

A container can be moved on top of a destination container only if there isn't already another container on top of the destination. The containers can be filled through an opening at the top of the container so they cannot be filled if there's already another container set on top of them.

The space on the floor is limited and the containers can be stacked up to a specified limit. Therefore, given the above constraints, the robot may have to sub-divide its goal into multiple sub-tasks and then make every possible effort to achieve a particular command.
