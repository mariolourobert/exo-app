# Basic Exoplayer app

This is a basic app that allows to manage a Playlist.

This app follows a clean architecture, divided in 3 main layers.

presentation (single module) - contains all the Screens and ViewModels
domain (single module) - contains the business logic (that is quite simple here) as multiples UseCases
data (folder that contains 2 modules):
- data - contains the repositories
- database - contains the Room database and the DAOs

Dependencies between layers don't follow the clean architecture by-the-book. Here the domain layer depends on the data layer.  It's intended, since it allows to build domain models (models returned by the domain layer) in the domain layer. The aim is to concentrate the logic in the domain layer and to let the presentation layer read domain models without much logic.

As presentation pattern, this app follows the Model-View-ViewModel design pattern, well known in the Android ecosystem.
