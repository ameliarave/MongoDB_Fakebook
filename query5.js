function oldest_friend(dbname){
    db = db.getSiblingDB(dbname)
    var results = {} 

    // Re-create flat_users collection from query2
    db.users.aggregate([
        {$project: {user_id: 1, friends: 1, _id: 0}},
        {$unwind: "$friends"},
        {$out: "flat_users"}
    ]);

    // Iterate flat_users collection adding friendships with lower ids
    db.flat_users.find().forEach(
        function(user){
            db.flat_users.insertOne({user_id: user.friends, friends: user.user_id});
        }
    );

    // Create dictionary mapping user_id's to their YOB
    var dict = {};
    var yob = db.users.find({}, {user_id: 1, YOB: 1, _id: 0});
    for (var i = 0; i < yob.length(); i++){
        dict[yob[i]["user_id"]] = yob[i]["YOB"];
    }


    // Iterate all flat_users 
    db.flat_users.find().forEach(
        function(doc){
            if(doc.user_id in results){   // if there's an entry for this id
                if(dict[doc.friends] < dict[results[doc.user_id]] || (dict[doc.friends] == dict[results[doc.user_id]] && doc.friends < results[doc.user_id])){
                    results[doc.user_id] = doc.friends;
                }
            }
            else{
                results[doc.user_id] = doc.friends;
            }
        }
    );

    return results;


};
