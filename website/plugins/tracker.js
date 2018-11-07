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

export default async ({app, store}, inject) => {
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
    }
  })
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
    view(category, label, others) {
      event.call(gtag, 'View', {category, label}, others)
    },
    qid(category, label, others) {
      event.call(gtag, 'QID', {category, label}, others)
    }
  })

  /**
   * Track each page view
   */
  app.router.beforeEach((to, from, next) => {
    const data = {
      'page_title': to.name,
      'page_path': to.fullPath
    }
    if (DEBUG) console.info('Tracker: PageView', data)
    else gtag('config', ID, data)

    next()
  })
}
