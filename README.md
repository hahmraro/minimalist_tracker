# Minimalist Tracker

This is a minimalist calorie tracker app with *natural language* API from CalorieNinjas.
Just search your food, and the API will automatically understand your query and add it to the tracker.
No fuss.

## Screenshots
<p float="left">
  <img src="https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot1.jpeg" width="32%" />
  <img src="https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot2.jpeg" width="32%" />
  <img src="https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot3.jpeg" width="32%" />
</p>
<p float="left">
  <img src="https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot4.jpeg" width="32%" />
  <img src="https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot5.jpeg" width="32%" />
  <img src="https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot6.jpeg" width="32%" />
</p>

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:hahmraro/minimalist_tracker.git
```

## Configuration
### API key:
The app uses its own API key, but it is advised that you go to the [CalorieNinjas API site](https://calorieninjas.com/api) and get your own free API key.
Then just replace the variable "API_KEY" in the module build.gradle file:
```
// Replace this string with your own key
def API_KEY = "YOUR API KEY HERE"
```
Or just change it in the settings page inside the app.

## Maintainers
This project is maintained by:
* [João Pegoraro](http://github.com/hahmraro)

## Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Run the [linter](https://ktlint.github.io/) (The [AndroidStudio plugin](https://plugins.jetbrains.com/plugin/15057-ktlint-unofficial-) works just fine too).
5. Push your branch (git push origin my-new-feature)
6. Create a new Pull Request