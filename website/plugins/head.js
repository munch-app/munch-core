// TODO(fuxing): This needs to be expanded to feature JSON-LD

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

const publisher = {
  "@type": "Organization",
  "name": "Munch",
  "logo": {
    "@type": "ImageObject",
    "url": "https://cdn.munch.app/eyJidWNrZXQiOiJtaDQiLCJrZXkiOiJsb2dvXzMwMC5wbmcifQ=="
  }
}

function scriptSchema(graph) {
  return [{
    type: 'application/ld+json',
    innerHTML: JSON.stringify({
        "@context": "http://schema.org",
        "@type": "Article",
        "publisher": publisher,
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

  const {title, description, image, url, type} = graph
  const meta = []
  if (title) {
    meta.push({hid: 'og:title', name: 'og:title', content: title})
  }
  if (description) {
    meta.push({hid: 'description', name: 'description', content: description})
    meta.push({hid: 'og:description', name: 'og:description', content: description})
  }
  if (url) {
    meta.push({hid: 'og:url', name: 'og:url', content: url})
  }
  /**
   * Url or app.munch.model.Image
   */
  if (image) {
    if (typeof image === "string") {
      meta.push({hid: 'og:image', name: 'og:image', content: image})
    } else if (image.sizes && image.sizes['640x640']) {
      meta.push({hid: 'og:image', name: 'og:image', content: image.sizes['640x640']})
    }
  }

  /**
   * Known types:
   * article, place, profile
   */
  if (type) {
    meta.push({hid: 'og:type', name: 'og:type', content: type})
  }
  return meta
}

/**
 * Populate the root head node
 */
function root({graph}) {
  if (graph?.title) {
    return {
      title: graph.title
    }
  }
}

function links({canonical}) {
  const links = []
  if (canonical) {
    links.push({rel: 'canonical', href: canonical})
  }
  return links
}

export default (context, inject) => {
  inject('head', ({graph, robots, breadcrumbs, meta, canonical}) => {
    return {
      ...root({graph}),

      // Links
      link: links({canonical}),

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
