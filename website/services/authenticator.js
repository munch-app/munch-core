/**
 * This is a browser only process
 */
import firebase from 'firebase/app'
import 'firebase/auth'

import * as Cookies from 'js-cookie'

firebase.initializeApp({
  apiKey: "AIzaSyAa_YPfjj3aLWjH2yzxY2qf23soSqWVLtE",
  authDomain: "munch-core.firebaseapp.com",
  databaseURL: "https://munch-core.firebaseio.com",
  projectId: "munch-core",
  storageBucket: "munch-core.appspot.com",
  messagingSenderId: "951023944206"
});

function getIdToken() {
  if (firebase.auth().currentUser) {
    return firebase.auth().currentUser.getIdTokenResult()
      .then(({expirationTime, token}) => {
        Cookies.set('IdToken', {expirationTime, token})
        return {expirationTime, token}
      })
  } else {
    Cookies.remove('IdToken')
    return Promise.resolve()
  }
}

export default {
  getIdToken: getIdToken,
  authenticated: () => getIdToken(),
  signInFacebook() {
    return firebase.auth().signInWithPopup(new firebase.auth.FacebookAuthProvider())
      .then(() => {
        return getIdToken()
      })
  },
  signOut() {
    firebase.auth().signOut()
    Cookies.remove('IdToken')
  },
  isLoggedIn() {
    return !!firebase.auth().currentUser
  }
}
