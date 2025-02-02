# ExoPlayer in Jetpack Compose

This repository is intended as a codelab of mine to showcase several types of Player in Jetpack Compose.
The plan is to show the implementation of ExoPlayer, like we see in Views. 

I created this because, I have a hard time to find an example of ExoPlayer implementation in Jetpack Compose
that maintain proper resource handling of the ExoPlayer. **Most of the Examples I've seen only `RELEASE` the resources when it goes `ON_DISPOSE` state**

I hope it can be useful for you.😊

💻 **Available sample**
------------
For now, the Player support these :
- A custom UI controller. (Currently it can only `Pause` and `Play` the Player)
- Resource handling. (Player will get `RELEASE` every time it goes into `ON_STOP` and `BUILD` every time it goes int `ON_START`).
- Saving last state of Playback everytime it goes `ON_STOP` and Retrieving the last state everytime it goes `ON_START`.
- Auto play on Network Reconnected.

🏢 **Structuring**
------------
For the implementation of the Builder and UI, you can see links below:
- Player Manager that consist Builder, Listener, and its states in here [PlayerManager.kt](https://github.com/medioka/compose-players-codelab/blob/master/normal_player/src/main/java/com/medioka/player/PlayerContent.kt)
- Player UI in here [PlayerContent.kt](https://github.com/medioka/compose-players-codelab/blob/master/normal_player/src/main/java/com/medioka/player/PlayerContent.kt)

📱**DEMO**
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
