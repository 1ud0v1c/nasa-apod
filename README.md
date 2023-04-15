<h1 align="center">NASA - Astronomy Picture of the Day</h1>

<p align="center">
  <a href="https://android-arsenal.com/api?level=21">
	<img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/>
  </a>
  <a href="https://github.com/1ud0v1c/nasa-apod/workflows/Unit%20Tests/badge.svg?event=push">
	<img alt="Unit Tests" src="https://github.com/1ud0v1c/nasa-apod/workflows/Unit%20Tests/badge.svg?event=push"/>
  </a>
  <a href="https://sonarcloud.io/dashboard?id=1ud0v1c_nasa-apod">
	<img alt="Quality Gate Status" src="https://sonarcloud.io/api/project_badges/measure?project=1ud0v1c_nasa-apod&metric=alert_status"/>
  </a>
</p>

Android application which list the last 30 photos of the [NASA's API](https://api.nasa.gov/): [Astronomy Picture of the Day](https://github.com/nasa/apod-api#docs-).

<div align="center">

![Application launcher](https://github.com/1ud0v1c/nasa-apod/blob/main/data/launcher.png)

<a href='https://play.google.com/store/apps/details?id=com.ludovic.vimont.nasaapod'>
	<img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height=90px/>
</a>

</div>

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

For the HomeActivity, I tried to provide a nice & efficient user experience, thanks to different state. If you launch the application without having internet, you will have a
screen which gives you a way to relaunch the request, In the same spirit, if you have a timeout or a server issue, you will ended with a different screen.

<div align="center">

![Home no internet](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_no_internet.png)
![Home network error](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_network_error.png)

</div>

If everything is ok, you will first end up with a loading screen to help the user be patient, then you will have with a fadeOut animation the list of images displayed. 

<div align="center">

![Home loading screen](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_loading.png)
![Home listing](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_listing.png)

</div>

The next time, you will launch the application, the data will be loaded from the database, thus you will end directly on the list. Some days, the API gives us some video instead of a classical image, 
to be able to offer a thumbnail in this case, I work on a solution : 
- For Youtube video, it is pretty easy, the website offer us a practical solution, based on the ID of the video, we can directly retrieve the thumbnail of it. So for example, in the flux, we can find something like 
this: https://www.youtube.com/embed/ictZttw3c98?rel=0. The ID here, is ictZttw3c98, so we can find the thumbnail using this url: https://img.youtube.com/vi/ictZttw3c98/0.jpg. Like proposed on this 
useful [stackoverflow post](https://stackoverflow.com/questions/8841159/how-to-make-youtube-video-thumbnails-in-android/8842839#8842839).
- For vimeo, it is a bit more complicated. To be able to recover the thumbnail, we need to make an API call. The API can give us, url like this: https://player.vimeo.com/video/438799770. The ID here is 438799770. So 
with the ID, we can interrogate the following API: https://vimeo.com/api/v2/video/438799770.json, we will receive in the flux, the desired thumbnail. Again, thank you [stackoverflow](https://stackoverflow.com/questions/1361149/get-img-thumbnails-from-vimeo).

Finally, I added 3 actions available in the action bar: 
- As, I use a database to avoid to always request the NASA API, you need to be able to request it anew to be up to date. That is why, I provided a refresh action button which gives the user the possibility to relaunch 
a request.
- I found it a bit frustrating to not be able to request more days to display. So, in order to increase the application life duration. I added a number picker, to choose the number of days to request. As soon as the 
user validated the new number of days, we launch a new request.
- The last action is very simple, but is still useful. It is the possibility to see the remaining quota for our API Key.

<div align="center">

![Home number picker](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_number_of_days_to_fetch.png)
![Home quota](https://github.com/1ud0v1c/nasa-apod/blob/main/data/home/home_quota.png)

</div>


## DetailActivity

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
