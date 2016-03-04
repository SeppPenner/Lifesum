# Lifesum
Sample project using Lifesum's api for searching and storing food suggestions

#Libraries Used
- okhttp from square, http client - http://square.github.io/okhttp/
- greenDao from greenRobot, ORM - http://greenrobot.org/greendao/
- otto from square, event bus - http://square.github.io/otto/
- icons from nucleoapp - https://nucleoapp.com/

#Project Structure
- *app* - The android application itself
- *lifesumdaogenerator* - A Java project to generate the data model, dao for database

The overall implementation follows the Flux concept proposed by Facebook (https://facebook.github.io/flux/), focusing on unidirectional data flow to tackle data inconsistencies in android applications. This is implemented on Android by following closely to an architecture brought forward by Luis G Valle (https://github.com/lgvalle/android-flux-todo-app), by using these core modules: View, Action, Dispatcher and Store.


