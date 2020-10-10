<h1 align="center">NASA - Astronomy Picture of the Day</h1>

<p align="center">
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
</p>

Android application which list the last 30 photos of the NASA's API: Astronomy Picture of the Day.


## Design

The main inspiration for the HomeActivity comes from the [Photo Application](https://www.behance.net/gallery/104945199/Photo-Application?tracking_source=search_projects_recommended%7Capplication%20photo) from [Nick Ermakov](https://www.behance.net/extezerofficial).

To be able to provide a great user experience when loading or when an error occurred while fetching the data, I browsed some really great projects, for example :
- [Error Illustrations - Empty States Vol 02](https://www.behance.net/gallery/57693817/Error-Illustrations-Empty-States-Vol-02?tracking_source=search_projects_recommended%7CAndroid%20empty%20state) from [Nimasha Perera](https://www.behance.net/nimashasperera) 
- [DAY 62-empty state](https://www.behance.net/gallery/53698651/DAY-62-empty-state) from [Shangnan Zhang](https://www.behance.net/Zhangshangnan)

To find the logo to display the current state of my adapter list, I used the website [Undraw](https://undraw.co/).


# Inspiration

I wanted to thank, those two applications which give me inspiration for design or even some actions (like set as wallpaper):
- [Resplash](https://github.com/b-lam/Resplash): A very effective application to find beautiful wallpaper for our smartphone powered by the website [Unsplash](https://unsplash.com/)
- [APODWallpaper](https://github.com/JakeSteam/APODWallpaper): An application which consumes the APOD API. 


# Architecture 

For this test, I used an [MVVM architecture](https://developer.android.com/jetpack/guide).

![MVVM architecture](https://github.com/1ud0v1c/nasa-apod/raw/master/data/mvvm-architecture.png)

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
