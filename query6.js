// query6 : Find the Average friend count per user for users
//
// Return a decimal variable as the average user friend count of all users
// in the users document.

function find_average_friendcount(dbname){
  db = db.getSiblingDB(dbname)
  // return a decimal number of average friend count
  var num_friend_arr = [];
  // Array of numbers giving length of each user's friend list 
  db.users.find().forEach(function(myDoc){num_friend_arr.push(myDoc.friends.length)});
  var total = 0.0
  // Summate all values in array to get total number of friends across all users' friends lists
  for(let i = 0; i < num_friend_arr.length; i++){
    total += num_friend_arr[i];
  }
  // Divide the sum by the number of users to get average
  return total / db.users.find().count();  
}
