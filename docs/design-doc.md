# URL Shortener - Design Doc

## Problem

Given a long URL, generate a short, unique alias that redirects to it.

## Requirements

### Functional

* Takes in a long URL, Return a short URL.
* GET short code, Redirect to the original long URL
* (Further enhancements) custom aliases, short URL expiration, click analytics

### Non-functional

* Read-heavy: redirects >> creations (current estimates \~100:1)
* Low-latency redirects (this is the user-facing hot path)
* Short codes must not collide
* Availability > strict consistency for redirects (Delay while looking up for the long URL is better than redirecting to the wrong address)

## Constraints

* Assume: 10M new URLs/day, 100:1 read/write ratio -> 1B redirects/day (\~11,500 req/sec average, plan for peak \~5x)
* Short code length: not fixed upfront using Base62(sequential ID).

  * The length grows naturally as we increment the ID starting from 0.
  * Expected counts in 10 years = 10M URL/day \* 365 day/year \* 10 years = 36.5 billion
  * 62^6 is roughly 56.8 billion which should cover the 10 years.
  * The ID can be safely stored as VARCHAR(10) in the schema.
  * The fixed-width sizing will be a concern when we switch to option 2 or 3 below.



## Options Considered

1. **Sequential ID + Base62 encode** : simple, no collisions thanks to sequential ID, but IDs are guessable/enumerable, and a single DB sequence is a scaling bottleneck because every time a new ID is needed, it needs to lock and update a single row. Multiple counters may give overlapping IDs, which requires coordination. Base62 instead of Base64 because + and /, which are not URL-safe, are excluded
2. **Hash the long URL (MD5/SHA-256), truncate, Base62 encode** : no central counter needed, but truncated hashes collide, so you need a collision-resolution strategy.
3. **Random Base62 string, check DB for collision, retry if exists** : no predictability, but the write path has to check-then-insert.

## Decision

First version of the application uses **Sequential ID + Base 62** for the simplicity. As the application scales and generating a short code becomes a bottleneck due to the nature of relying a single database for the next sequential number, then we will need to pivot to either option 2 or option 3 above.



Since this is a simple demo URL shortener, it is not expected to deal with sensitive data. Hence, predictability of sequential ID is an acceptable tradeoff for this project. In actual production dealing with private links, further measures, XOR or bit-shuffling against a fixed key should be introduced to prevent predictable enumeration.



## Redirect-at-scale plan

* Cache-aside pattern: check Redis first, fall back to Postgres on miss, populate cache on read.
* Cache eviction: LRU, since older/unpopular links matter less than recently-hit ones.

## Additional enhancements / risks

* Custom alias collisions. Potential approach, simply return an error and inform user to try a different alias
* Deleted/expired links. Potential approach, when a link is deleted or expired, explicitly remove it from Redis and from the Database. Additionally, each URL has its own TTL (24 hours) as a safety net.
* Rate limiting. Potential approach, introduce a rate limiter on the /shorten endpoint to prevent a single client from creating excessive short URLs.

