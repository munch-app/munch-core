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
  return new Promise(function (resolve, reject) {
    function complete(user) {
      user.getIdTokenResult()
        .then(({expirationTime, token}) => {
          Cookies.set('IdToken', {expirationTime, token})
          resolve({expirationTime, token})
        })
        .catch(err => {
          Cookies.remove('IdToken')
          reject(err)
        })
    }


    if (firebase.auth().currentUser) {
      complete(firebase.auth().currentUser)
    } else {
      const unsubscribe = firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
          complete(user)
        } else {
          Cookies.remove('IdToken')
          reject()
        }
        unsubscribe()
      })
    }
  })
}

export default {
  getIdToken,
  signInFacebook() {
    return firebase.auth().signInWithPopup(new firebase.auth.FacebookAuthProvider())
      .then(() => getIdToken())
  },
  signOut() {
    firebase.auth().signOut()
    Cookies.remove('IdToken')
  },
  isLoggedIn() {
    return !!firebase.auth().currentUser
  }
}
