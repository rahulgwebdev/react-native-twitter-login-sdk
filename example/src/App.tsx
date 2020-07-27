import * as React from 'react';
import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import TwitterLoginSdk from 'react-native-twitter-login-sdk';

const Constants = {
  //Dev Parse keys
  TWITTER_COMSUMER_KEY: 'ZIM2EsgAdVOz1YX7O7X8rtsyw', // APi key
  TWITTER_CONSUMER_SECRET: '5E5obL7jCOPWkKTB3Rlmi0QgWDfx3v4DWqpVbCluY0b1VC7q9m', // API key secret
};

export default function App() {
  // const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    // TwitterLoginSdk.multiply(3, 7).then(setResult);
    TwitterLoginSdk.initialize(
      Constants.TWITTER_COMSUMER_KEY,
      Constants.TWITTER_CONSUMER_SECRET
    ).then((res) => console.log(res));
  }, []);

  const login = async () => {
    try {
      const data = await TwitterLoginSdk.logIn();
      console.log('data :', data);
    } catch (error) {
      console.log(error);
    }
  };

  const logout = () => {
    TwitterLoginSdk.logOut();
  };

  return (
    <View style={styles.container}>
      {/* <Text>Result: {result}</Text> */}
      <TouchableOpacity onPress={login}>
        <Text>Twitter Login</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={logout}>
        <Text>Twitter Logout</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
