import { NativeModules } from 'react-native';

type TwitterLoginSdkType = {
  multiply(a: number, b: number): Promise<number>;
};

const { TwitterLoginSdk } = NativeModules;

export default TwitterLoginSdk as TwitterLoginSdkType;
