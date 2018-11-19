function scriptBreadcrumbs(breadcrumbs) {
  if (!breadcrumbs) return []

  const list = breadcrumbs.map(function (obj, index) {
    return {
      "@type": "ListItem",
      "position": index + 1,
      "name": obj.name,
      "item": obj.item
    }
  })

  if (list.length < 1) return []

  return [{
    type: 'application/ld+json',
    innerHTML: JSON.stringify({
        "@context": "http://schema.org",
        "@type": "BreadcrumbList",
        "itemListElement": list
      }
    )
  }]
}

function metaRobots(robots) {
  if (!robots) return []

  const {follow, index} = robots
  return [
    {name: 'robots', content: `${follow ? 'follow' : 'nofollow'},${index ? 'index' : 'noindex'}`}
  ]
}

function metaGraph(graph) {
  if (!graph) return []

  const {title, description, image, url, type, keywords} = graph
  const meta = []
  if (title) {
    meta.push({hid: 'og:title', name: 'og:title', content: title})
  }
  if (description) {
    meta.push({hid: 'description', name: 'description', content: description})
    meta.push({hid: 'og:description', name: 'og:description', content: description})
  }
  if (image) {
    meta.push({hid: 'og:image', name: 'og:image', content: image})
  }
  if (url) {
    meta.push({hid: 'og:url', name: 'og:url', content: url})
  }
  if (type) {
    meta.push({hid: 'og:type', name: 'og:type', content: type})
  }
  if (keywords) {
    meta.push({hid: 'keywords', name: 'keywords', content: keywords})
  }
  return meta
}

/**
 * Populate the root head node
 */
function root({graph}) {
  if (graph && graph.title) {
    return {
      title: graph.title
    }
  }
}

export default (context, inject) => {
  inject('head', ({graph, robots, breadcrumbs, meta}) => {
    return {
      ...root({graph}),

      // Metas from Graph & Robot
      meta: [
        ...metaRobots(robots),
        ...metaGraph(graph),
        // Allows raw meta
        ...(meta || []),
      ],

      // Scripts
      __dangerouslyDisableSanitizers: ['script'],
      script: [
        ...scriptBreadcrumbs(breadcrumbs)
      ],
    }
  })
}