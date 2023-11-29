// fill in your database name
// Your dbname is your uniqname
var dbname = 'uniqname';

var l1 = load('query1.js')
var l2 = load('query2.js')
var l3 = load('query3.js')
var l4 = load('query4.js')
var l5 = load('query5.js')
var l6 = load('query6.js')
var l7 = load('query7.js')
var l8 = load('query8.js')

// test query1
print("===Test1===")
var test1 = find_user('Bucklebury', dbname);
print(test1); // uncomment this line to print the query1 output
var ans1 = test1.length;
if(ans1 == 42){
	print("Local test passed! Partially correct.");
} else {
	print("Incorrect!");
	print("Expepcting 42 users from Bucklebury, ", ans1, " are found.");
}


// test query2
print("===Test2===")
unwind_friends(dbname);
var test2 = db.flat_users.find();
test2; // uncomment this line to print the query2 output
var ans2 = test2.count();
if(ans2 == 21355){
	print("Local test passed! Partially correct.");
} else {
	print("Local test failed! ");
	print("Expecting 21355 pairs of friends, ", ans2, " pairs found.");
}


// test query3
print("===Test3===")
cities_table(dbname);
var test3 = db.cities.find({"_id" : "Bucklebury"}).next().users;
print(test3); // uncomment this line to print the query3 output
var ans3 = test3.length;
if(ans3 == 43){
	print("Local test passed! Partially correct.");
} else {
	print("Incorrect.");
	print("Expecting 43 users living in Bucklebury, ", ans3, " found.");
}


// test query4
print("===Test4===")
var test4 = suggest_friends(5,dbname);
print(test4); // uncomment this line to print the query4 output
var ans4 = test4.length;
if (ans4 == 87){
	print("Local test passed! Partially correct.");
} else {
	print("Incorrect.");
	print("Expecting 87 pairs of suggested friends, ", ans4, " pairs found.");
}


// test query5
print("===Test5=== (This test is time comsuming)")
var test5 = oldest_friend(dbname);
test5; // uncomment this line to print the query5 output
if(Object.keys(test5).length == 798){
	if(test5.hasOwnProperty(799)){
		var ans5 = test5[799];
		if(test5[799] == 51){
			print("Local test passed! Partially correct.");
		} else {
			print("Incorrect.")
			print("Oldest friend for user 799 is 51, you found ", ans5)
		}
	} else {
		print("Incorrect.")
		print("No oldest friend for user 799.")
	}
} else {
	print("Incorrect.")
	print("Result should have 798 oldest friends, ", Object.keys(test5).length, " found.")
}


// test query6
print("===Test6===")
var ans6 = find_average_friendcount(dbname);
print(ans6); // uncomment this line to print the query6 output
if (ans6 > 26 & ans6 < 27) {
	print("Local test passed! Partially correct.");
} else {
	print("Incorrect.");
	print("The average number of friends is between 26 to 27, you got ", ans6);
}


// Test query 7 with map reduce
print("===Test7===")
var result7 = db.users.mapReduce(
	num_month_mapper,
	num_month_reducer,
	{
		out: "born_each_month",
		finalize: num_month_finalizer
	});
var test7 = db.born_each_month.find();
test7; // uncomment this line to print the query7 output
var ans7 = test7.count();
if (ans7 == 12) {
	print("Local test passed! Partially correct.");
} else {
	print("Incorrect.");
	print("Expecting 12 months(keys), you returned ", ans7);
}

// test query 8 with map reduce
print("===Test8===")
var result8 = db.users.mapReduce(
  city_average_friendcount_mapper,
  city_average_friendcount_reducer,
  {
	out: "friend_city_population",
		finalize: city_average_friendcount_finalizer
  }
);
var test8 = db.friend_city_population.find();
test8; // unconmment this line to print the query8 output
var ans8 = test8.count();
if (ans8 == 16) {
	print("Local test passed! Partially correct.");
} else {
	print("Incorrect.");
	print("Expecting 16 cities(keys), you returned ", ans8);
}