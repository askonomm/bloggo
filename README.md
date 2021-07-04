# Bloggo

Bloggo is a blogging platform I'm making for myself. The idea is to have a super fast and lean blog system with no themes (just one, built-in theme), a block-based editor that isn-t bloated (looking at you WordPress), with newsletter support and that would be simply a joy to use.

Current status: I would not use it yet if I were you. I mean, I don't even use it myself yet. However, you can install it and give it a shot if you want.

## Installation

I do eventually plan to make this whole thing a single binary, but for now you can just clone the repository and then run `npx shadow-cljs release app` and run the resulting `bloggo.js` with `node bloggo.js` as per usual, and visit `localhost:3000/admin` to finish setting it up. Oh and, you need to come with the database yourself right now from the code as I can't be bothered with it at the moment.