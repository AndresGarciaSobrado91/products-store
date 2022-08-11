# products-store
An Android app about a product store with Clean Architecture

## 🏗️️ Built with
| What                    | How                        |
|----------------         |------------------------------    |
| 📝  Language            | [Kotlin](https://kotlinlang.org/)                            |
| 🏗  Architecture        | [MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)                            |
| 🧠  API                 | [GitHub Gist](https://gist.githubusercontent.com/palcalde/6c19259bd32dd6aafa327fa557859c2f/raw/ba51779474a150ee4367cda4f4ffacdcca479887/Products.json)                            |
| 💾  Persistence         | [Room](https://developer.android.com/training/data-storage/room)                            |
| 💉  DI                  | [Dagger-Hilt](https://dagger.dev/hilt/)                        |
| 🛣️  Navigation          | [Jetpack Navigation Component](https://developer.android.com/guide/navigation)                        |
| 🌊  Async               | [Coroutines + Flow + LiveData](https://kotlinlang.org/docs/coroutines-overview.html)                |
| 🌐  Networking          | [Retrofit](https://github.com/square/retrofit)                        |
| 📄  JSON                | [Gson](https://github.com/google/gson)                            |

## 📷 Screenshots
![image](https://user-images.githubusercontent.com/48637183/184250472-7295d4e0-c7ea-4185-bc02-fa6b4333ac9a.png)
![image](https://user-images.githubusercontent.com/48637183/184250580-f730b5c8-84b0-45c4-a001-f9f86195f6ff.png)
![image](https://user-images.githubusercontent.com/48637183/184250665-bb7ffc28-006f-45f5-a194-c2987ba07196.png)

## 📓 Notes
The main solution consists of a basket of products that the user updates while using the app. It is persisted in a local database.

The idea is that both, products and promotions are fetched from a backend server using different endpoints. The products and the promotions are merged and we get a unique domain object containing this data.

The promotion class was designed in order to be able to add new types of discounts in the future, without needing to change the code.

The user can add or remove products from the basket and the total price of the cart is displayed in the home fragment.

Once the user is ready to checkout, the checkout fragment shows all the items in the basket and the corresponding discounts if it corresponds. Here the user can decide to remove certain items and continue to the final payment screen.

Not all the tests were added for a matter of time, but I consider that the most relevant ones are written.

App is currently locked to portrait mode for a matter of time.
