This package is to play with ideas for a distributed systems design question I saw recently:

"Design a service that the IMDb web front end would use to show a list of top ten movies by page views."

For clarification:
* There will be 100's of web front end nodes.
* The list of top ten movies will be shown in a side bar on evey page of the site.
* The site has ~ 10 ** 7 users
* The site has ~ 10 ** 8 page views per day
* A single hot movie may have up to ~ 10 ** 7 page views per day
* There are ~ 10 ** 6 movies in the database
* The service should support returning the list of top ten movies by page view for the current day
  (stretch goal: last 24 hours)
* Somewhat stale data is acceptable (i.e. don't need to count data from the last n minutes), but the degree of staleness
  should be minimised. Can we get staleness down to 1 minute?
* Somewhat inaccurate data is acceptable, i.e. page views can be dropped under unusual load or failure scenarios
* The service should be highly available
* The service should be horizontally scalable i.e. by adding more service nodes we should be able to count:
  - 10x the page views on a hot movie
  - 10x the total page views on movies
  - 10x the movies
