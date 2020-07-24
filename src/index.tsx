import { NativeModules } from 'react-native';

type TwitterLoginSdkType = {
  init(consumerKey: String, consumerSecret: String): Promise<any>;
  logIn(): Promise<any>;
  logOut(): void;
};

const { TwitterLoginSdk } = NativeModules;

export default TwitterLoginSdk as TwitterLoginSdkType;
