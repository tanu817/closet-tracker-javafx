# Closet Tracker

A JavaFX desktop app for organising your wardrobe, built as a group project for the 2IRR00 course at Eindhoven University of Technology (TU/e) in 2026.

## What it does

- **Add clothing items** with a name, image, and description, and sort them with customisable tags such as colour, weather, occasion, or style.
- **Search by tags** to filter your closet. Pick a top and it suggests bottoms that match its tags.
- **Create outfits** and save your favourites.
- **Track how often things are worn**, for items and for whole outfits.
- **Dashboard** with wardrobe statistics: most and least worn items, recently worn outfits, favourite combinations, and a pie chart of clothing categories.

The idea is to make outfit planning easier and to encourage sustainable habits by showing how much each piece actually gets used.

## The team

This was a group project by Group 21, with six members:

- Aleksandra Levenkova
- Alexandra Varapai
- Arshiya Tasneem Shaik
- Bhavini Kopparam Ajay
- Priyanshu Guha
- Tanusri Madhalam

The commit history is preserved from the original course repository, so everyone's contributions are credited. Some teammates' email addresses were replaced with placeholder addresses to protect their privacy, and files that were only part of the course submission (reports and reflections) were left out.

My own work included the dashboard statistics (the pie chart) and Checkstyle clean-ups. The `stats-dashboard` and `debugging+extra_features` branches hold some of those commits.

## Tech

- Java 21 and JavaFX 21
- Maven
- Data stored in plain CSV files (`clothing_items.csv`, `clothing_tags.csv`, `outfits.csv`, `outfit_items.csv`, `tags.csv`)
- JUnit 5 and TestFX for unit, integration, and UI tests
- Checkstyle for code style

## Running it

You need Java 21 and Maven. From the repository root:

```bash
mvn clean javafx:run
```

To run the tests:

```bash
mvn test
```

The sample CSV files in the repo give the app some starting data.

## Project structure

```text
src/main/java/com/mycompany/irr00_group_project/
  model/        data classes (items, outfits, tags, wear events)
  database/     repositories that read and write the CSV files
  service/      app logic (search, outfits, recommendations, dashboard)
  controller/   connects the pages to the services
  ui/           JavaFX pages
  utils/        helpers (validation, images, tags)
src/test/java/  unit, integration, and UI tests
```
