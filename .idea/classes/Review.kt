//review class

class Review(
    val user: UserAccount,
    val store: Store,
    val overAllRating: Double,
    val comment:String,
    val dateTime:LocalDateTime,//local date time objects

    //do we need optional other rating types?? cleanliness etc
)
