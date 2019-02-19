const ID = 'UA-117480436-1'
const DEBUG = process.env.NODE_ENV !== 'production'
  || process.env.origin !== 'https://www.munch.app'

// gtag script is injected in the nuxt.config.js head

function event(action, {category, label, value}, others) {
  const data = {'event_category': category}

  if (label) data['event_label'] = label
  if (value) data['value'] = value

  if (others) {
    Object.keys(others).forEach(key => {
      if (others[key]) {
        data[key] = others[key]
      }
    })
  }

  if (DEBUG) console.info('Tracker: Event', {action, data})
  else this('event', action, data)
}

export default ({app, store}, inject) => {
  // @formatter:off
  window.dataLayer = window.dataLayer || []
  function gtag(){dataLayer.push(arguments)}

  gtag('js', new Date());
  gtag('config', ID, {
    'send_page_view': false,
    'custom_map': {
      'dimension1': 'from_action',
      'dimension4': 'from_category',
      'dimension3': 'from_search_location_type',
      'dimension5': 'id',
    }
  })

  // Set the user ID using signed-in user_id.
  const userId = store.state.user.profile && store.state.user.profile.userId;
  if (userId) gtag('set', {'user_id': userId});
  // @formatter:on

  /**
   * For clarity: Event Actions show be clicks that user performed, should not be routing hooks
   *
   * Top level Actions
   * - Share
   * - Search
   * - View
   *
   * @param category for event      required
   * @param label for event         optional
   * @param others dimension        optional
   */
  inject('track', {
    share(category, label, others) {
      event.call(gtag, 'Share', {category, label}, others)
    },
    search(category, label, others) {
      event.call(gtag, 'Search', {category, label}, others)
    },
    qid(category, label, others) {
      event.call(gtag, 'QID', {category, label}, others)
    },
    collection(category, label, others) {
      event.call(gtag, 'Collection', {category, label}, others)
    },
    link(category, label, others) {
      event.call(gtag, 'Link', {category, label}, others)
    },
    login(category, label, others) {
      event.call(gtag, 'Login', {category, label}, others)
    },
    download(category, label, others) {
      event.call(gtag, 'Download', {category, label}, others)
    },

    // New recommended tracking (>0.19.0), it follows the Native App Tracking
    view(name, label, others) {
      event.call(gtag, 'View', {category: name, label}, others)
    },
    click(name, label, others) {
      event.call(gtag, 'Click', {category: name, label}, others)
    },

    setUserId(userId) {
      gtag('set', {'user_id': userId});
    },
    clearUserData() {

    }
  })

  /**
   * Track each page view
   */
  app.router.beforeEach((to, from, next) => {
    // Never Track Authenticate Page
    if (to.name === 'authenticate') return next()

    const data = {
      'page_title': to.name,
      'page_path': to.fullPath
    }
    if (DEBUG) console.info('Tracker: PageView', data)
    else gtag('config', ID, data)

    next()
  })
}
