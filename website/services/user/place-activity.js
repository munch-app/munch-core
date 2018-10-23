const KEY = 'user-place-activity'

const store = process.client ? require('store/dist/store.modern') : {}

const NavigationAction = {
  bannerImageItem: (index) => `navigation_banner_image_item(${index})`,
  partnerInstagramItem: (index) => `navigation_partner_instagram_item(${index})`,
  partnerArticleItem: (index) => `navigation_partner_article_item(${index})`
}

const ClickAction = {
  hours: () => 'click_hours',
  mapExternal: () => 'click_map_external',
  partnerInstagramItem: (index) => `click_partner_instagram_item(${index})`,
  partnerArticleItem: (index) => `click_partner_article_item(${index})`,

  // Not Implemented Yet
  award: (collectionId) => `click_award${collectionId}`,
  about: () => 'click_about',
  menuImageItem: (index) => `click_menu_image_item(${index})`,
  menuWeb: () => 'click_menu_web',
  suggestEdit: () => 'click_suggest_edit',
  tag: () => 'click_tag',
  addedToCollection: () => 'click_added_to_collection',

  // Not Supported
  screenshot: () => 'click_screenshot',
  mapHeading: () => 'click_map_heading',
  map: () => 'click_map',
  direction: () => 'click_direction',
  call: () => 'click_call',
  partnerInstagram: () => 'click_partner_instagram',
  partnerArticle: () => 'click_partner_article',
}

function update(update) {
  return new Promise(resolve => {
    const map = store.get(KEY) || {}
    const result = update.call(this, map)
    store.set(KEY, map)

    resolve(result)
  });
}

function push(placeId, startedMillis, actions) {
  const mappedActions = Object.keys(actions).map(function (key) {
    return {name: key, millis: actions[key]}
  })

  const activity = {
    placeId,
    platform: 'Web',
    startedMillis: startedMillis,
    actions: mappedActions
  }

  console.log(activity)
  if (this.$store.getters['user/isLoggedIn']) {
    const authenticator = require('~/services/authenticator').default
    return authenticator.authenticated().then(() => {
      return this.$axios.$put(`api/users/places/activities/${activity.placeId}/${activity.startedMillis}`, activity)
        .catch(error => {
          return this.$store.dispatch('addError', error)
        })
    })
  }
}

/**
 *
 * @param placeId id of place
 * @param action performed by the user
 * @see NavigationAction
 * @see ClickAction
 */
function add(placeId, action) {
  return update(map => {
    if (map[placeId]) {
      map[placeId].actions[action] = new Date().getTime()
    }
  })
}

export const navigation = Object.keys(NavigationAction).reduce(function (object, key) {
  object[key] = (placeId, data) => {
    const name = NavigationAction[key](data)
    return add(placeId, name)
  }
  return object
}, {})

export const click = Object.keys(ClickAction).reduce(function (object, key) {
  object[key] = (placeId, data) => {
    const name = ClickAction[key](data)
    return add(placeId, name)
  }
  return object
}, {})

/**
 * @param placeId
 * @return startMillis in a promise
 */
export function start(placeId) {
  return update(map => {
    const now = new Date().getTime()

    // Push updates to server first that expected end has surpassed
    Object.keys(map).forEach(placeId => {
      if (map[placeId].expectedEndMillis < now) {
        push.call(this, placeId, map[placeId].startedMillis, map[placeId].actions)
        delete map[placeId]
      }
    })

    if (!map[placeId]) {
      map[placeId] = {
        placeId,
        startedMillis: now,
        actions: {}
      }
    }

    // Extend session that is alive for more then 30 minutes
    map[placeId].expectedEndMillis = now + (1000 * 60 * 30)
  })
}

