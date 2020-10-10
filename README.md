<h1 align="center">NASA - Astronomy Picture of the Day</h1>

<p align="center">
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
</p>

Android application which list the last 30 photos of the [NASA's API](https://github.com/1ud0v1c/nasa-apod/blob/main/data/mvvm-architecture.png): [Astronomy Picture of the Day](https://github.com/nasa/apod-api#docs-).


## Design

The main inspiration for the HomeActivity comes from the [Photo Application](https://www.behance.net/gallery/104945199/Photo-Application?tracking_source=search_projects_recommended%7Capplication%20photo) from [Nick Ermakov](https://www.behance.net/extezerofficial).

To be able to provide a great user experience when loading or when an error occurred while fetching the data, I browsed some really great projects, for example :
- [Error Illustrations - Empty States Vol 02](https://www.behance.net/gallery/57693817/Error-Illustrations-Empty-States-Vol-02?tracking_source=search_projects_recommended%7CAndroid%20empty%20state) from [Nimasha Perera](https://www.behance.net/nimashasperera) 
- [DAY 62-empty state](https://www.behance.net/gallery/53698651/DAY-62-empty-state) from [Shangnan Zhang](https://www.behance.net/Zhangshangnan)

To find the logo to display the current state of my adapter list, I used the website [Undraw](https://undraw.co/).


## Inspiration

I wanted to thank, those two applications which give me inspiration for design or even some actions (like set as wallpaper):
- [Resplash](https://github.com/b-lam/Resplash): A very effective application to find beautiful wallpaper for our smartphone powered by the website [Unsplash](https://unsplash.com/)
- [APODWallpaper](https://github.com/JakeSteam/APODWallpaper): An application which consumes the APOD API. 


## HomeActivity

> L’écran d’accueil listera les photos (présentation libre)

For the HomeActivity, I tried to provide a nice & efficient user experience, thanks to different state. If you launch the application without having internet, you will have a screen which gives you a way to relaunch 
the request, In the same spirit, if you have a timeout or a server issue, you will ended with a different screen. 

<div align="center">

![Home no internet](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_no_internet.png)
![Home network error](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_network_error.png)

</div>

If everything is ok, you will first end up with a loading screen to help the user be patient, then you will have with a fadeOut animation the list of images displayed. 

<div align="center">

![Home loading screen](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_loading.png)
![Home listing](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_listing.png)

</div>

The next time, you will launch the application, the data will be loaded from the database, thus you will end directly on the list.


## DetailActivity

> Tandis qu’un écran de détail affichera la photo ainsi que sa description et les informations relatives. 

After a click on an item from the HomeActivity, you will enter in the DetailActivity, which list every information about the photo of the day.

<div align="center">

![DetailActivity screen](https://github.com/1ud0v1c/nasa-apod/blob/main/data/detail/detail_display.png)

</div>

You will also be able to do two actions. Share the URL to the website of the Nasa about the current photo 

<div align="center">

![DetailActivity share action](https://github.com/1ud0v1c/nasa-apod/blob/main/data/detail/detail_share_action.png)

</div>

Use the HD version of the image as wallpaper for your smartphone. 

![DetailActivity wallpaper download](https://github.com/1ud0v1c/nasa-apod/blob/main/data/detail/detail_wallpaper_progress.png)
![DetailActivity wallpaper error](https://github.com/1ud0v1c/nasa-apod/blob/main/data/detail/detail_wallpaper_error.png)
![DetailActivity wallpaper result](https://github.com/1ud0v1c/nasa-apod/blob/main/data/detail/detail_wallpaper_result.png)


## ZoomActivity

> Enfin, un troisième écran permettra de visualiser la photo en haute résolution.

Last, but not least, the ZoomActivity, entered after a click on the photo from the DetailActivity, allow an user to access the HD version of the image and can zoom on it.

<div align="center">

![ZoomActivity loading](https://github.com/1ud0v1c/nasa-apod/blob/main/data/zoom/zoom_loading.png)
![ZoomActivity loaded](https://github.com/1ud0v1c/nasa-apod/blob/main/data/zoom/zoom_loaded.png)

</div>

You will enter in an interactive mode and will be able to zoom or move on the photo.

<div align="center">

![ZoomActivity zoom interaction](https://github.com/1ud0v1c/nasa-apod/blob/main/data/zoom/zoom_interaction.png)

</div>

A landscape experience will be better because the majority of the photo are well suited for landscape.

![ZoomActivity landscape](https://github.com/1ud0v1c/nasa-apod/blob/main/data/zoom/zoom_landscape.png)


## Architecture 

For this test, I used an [MVVM architecture](https://developer.android.com/jetpack/guide).

![MVVM architecture](https://github.com/1ud0v1c/nasa-apod/blob/main/data/mvvm-architecture.png)

We can check the HomeActivity implementation :
- HomeActivity, it is the View himself, which can interact with the user
- HomeViewModel, the man-in-the middle classes which make the linking between the View and the Model
- HomeRepository, last but not least the class which does all the business logic. Generally speaking, this class handle the call to API or database.

The main advantage of this type of architecture, it is the capacity to easily test the business logic, We just have to test the repositories classes. 
And with dependency injection, it is even easier.


## External dependencies

- [Retrofit](https://github.com/square/retrofit): A type-safe HTTP client for Android. I used a moshi converter. I tend to use [moshi](https://github.com/square/moshi) over gson or jackson 
since I saw [that talk](https://www.youtube.com/watch?time_continue=2526&v=1PwdqkKDCSo&feature=emb_logo). Moshi seems to better handle accent and error than Gson and is much smaller 
than Jackson. 
- For my database, I chose [Room](https://developer.android.com/topic/libraries/architecture/room) for the efficiency to handle entities and database access.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html): Light-weight thread implementation. I like the readability and the simplicity of coroutine.
- [Glide](https://github.com/bumptech/glide): Fast and efficient open source image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling. 
Easy to use and easily configurable, it was the perfect library for this test.
- [Koin](https://github.com/InsertKoinIO/koin): I used Koin for dependency injection.
- [PhotoView](https://github.com/chrisbanes/PhotoView): A wrapper of an ImageView which supports a zooming experience. Very appropriate to display HD images.
