// query1 : find users whose hometown citys the specified city. 

function find_user(city, dbname){
    db = db.getSiblingDB(dbname);
    var results = [];
    // TODO: return a Javascript array of user_ids. 
    // db.users.find(...);
    db.users.find({"hometown.city": city}).forEach(
        function(users) {
            results.push(users.user_id);
        }
    );
    // See test.js for a partial correctness check.  
    // The result will be an array of integers. The order does not matter.
                                                               
    return results;
}
