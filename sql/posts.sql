CREATE TABLE IF NOT EXISTS "posts" (
	"id" integer primary key,
	"title" varchar,
	"slug" varchar,
	"entry" text, 
	"type" varchar, 
	"status" varchar, 
	"date" bigint
);
