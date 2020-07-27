
@objc(TwitterLoginSdk)
class TwitterLoginSdk: NSObject {

    @objc static func requiresMainQueueSetup() -> Bool {
        return false
    }
    
    @objc(initialize:withB:withResolver:withRejecter:)
    func initialize(consumerKey:String, consumerSecret:String, resolve: @escaping RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        DispatchQueue.main.async {
            TWTRTwitter.sharedInstance().start(withConsumerKey:consumerKey, consumerSecret:consumerSecret)
            resolve(true)
        }
    }
    
    @objc(logIn:withRejecter:)
    func logIn(resolve: @escaping RCTPromiseResolveBlock,reject: @escaping RCTPromiseRejectBlock) -> Void {
        
        DispatchQueue.main.async {
            TWTRTwitter.sharedInstance().logIn(completion: { (session, error) in
                if (session != nil) {
                    let client = TWTRAPIClient.withCurrentUser()
                    client.requestEmail { email, error in
                        let requestedEmail = (email == nil) ? "" : email
                        let result: NSMutableDictionary = [:]
                        result["email"] = requestedEmail
                        result["authToken"] = session?.authToken
                        result["authTokenSecret"] = session?.authTokenSecret
                        result["userName"] = session?.userName
                        result["userID"] = session?.userID
                        client.loadUser(withID: session!.userID, completion: { (user, error) in
                            result["name"] = user?.name
                            result["profileImageURL"] = user?.profileImageURL
                            resolve(result);
                        })
                    }
                } else {
                    reject("Error", "Twitter signin error", error);
                }
            })
        }
    }
    
    @objc(logOut)
    func logOut() -> Void {
        DispatchQueue.main.async {
        let store = TWTRTwitter.sharedInstance().sessionStore
            if let userID = store.session()?.userID {
                store.logOutUserID(userID)
            }
        }
    }
}
