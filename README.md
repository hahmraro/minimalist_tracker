# Minimalist Tracker

This is a minimalist calorie tracker app with *natural language* API from CalorieNinjas.
Just search your food, and the API will automatically understand your query and add it to the tracker.
No fuss.

## Screenshots
![Diary](https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot1.jpeg)
![Search/History](https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot2.jpeg)
![Food nutrition](https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot3.jpeg)
![Total Nutrition](https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot4.jpeg)
![Ate too much calories for the day](https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot5.jpeg)
![Dark mode](https://github.com/hahmraro/minimalist_tracker/blob/master/screenshots/Screenshot6.jpeg)

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:hahmraro/minimalist_tracker.git
```

## Configuration
### API key:
Go to the [CalorieNinjas API site](https://calorieninjas.com/api) and get your free API key.
Create `local.properties` in the root of the project and add this line:
```gradle
apiKey="*YOUR API KEY HERE*"
```
Replacing where it says "YOUR API KEY HERE" with your own key that you got from the API site.
Now just build the project and everything should work fine.

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