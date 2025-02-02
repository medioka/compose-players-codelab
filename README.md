# ExoPlayer in Jetpack Compose

This repository is intended as a codelab of mine to handle many type of players in Jetpack Compose.
The plan is to show the implementation of ExoPlayer, like we see in Views. 

I created this because, I have a hard time to find an example of ExoPlayer implementation in Jetpack Compose,
while maintaining proper resource handling of the ExoPlayer. **NOT only get `RELEASE` when it goes `ON_DISPOSE` state**

I hope it can be useful for you.😊

💻 **Available sample**
------------
For now, the Player support these :
- A custom UI controller. (Currently it can only `Pause` and `Play` the Player)
- Resource handling. (Player will get `RELEASE` every time it goes into `ON_STOP` and `BUILD` every time it goes int `ON_START`).
- Saving last state of Playback everytime it goes `ON_STOP` and Retrieving the last state everytime it goes `ON_START`.
- Auto play on Network Reconnected.

📱 **DEMO**
------------
![player_controller](https://github.com/user-attachments/assets/c8bf8996-6cd7-4382-9010-ca7ea9ed8534)


📝 **TODO**
------------
- Implement seeker on the current player. (My custom seeker is still on Development).
- Implement player LANDSCAPE mode.
- Provide a sample to change `Resolution`, `Playback Speed`, and `Caption`.

🔮 **Future Plan**
------------
- Create an implementation of player in Reels. (Still researching on how to implement that properly).
- Create an implementation of player in Grid.
