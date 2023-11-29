
// query 4: find user pairs (A,B) that meet the following constraints:
// i) user A is male and user B is female
// ii) their Year_Of_Birth difference is less than year_diff
// iii) user A and B are not friends
// iv) user A and B are from the same hometown city
// The following is the schema for output pairs:
// [
//      [user_id1, user_id2],
//      [user_id1, user_id3],
//      [user_id4, user_id2],
//      ...
//  ]
// user_id is the field from the users collection. Do not use the _id field in users.
  
function suggest_friends(year_diff, dbname) {
    db = db.getSiblingDB(dbname);
    var pairs = [];
    
    // TODO: implement suggest friends
    db.users.find({"gender": "female"}).forEach(function(user_A) {
        db.users.find({"gender": "male"}).forEach(function(user_B) {
            // part iv) same hometown
            if (user_A.hometown.city == user_B.hometown.city) {
                // part iii) not friends
                if (user_A.friends.indexOf(user_B.user_id) == -1 && user_B.friends.indexOf(user_A.user_id) == -1) {
                    // part ii) yob gap < year_diff
                    if (Math.abs(user_A.YOB - user_B.YOB) < year_diff) {
                        // under all of these conditions, add to the array
                        pairs.push([user_A.user_id, user_B.user_id]);
                    }
                }
            }            
        })
    });
    // Return an array of arrays.
    return pairs;
}
