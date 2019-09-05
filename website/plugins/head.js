import dateformat from 'dateformat'

const publisher = {
  "@type": "Organization",
  "name": "Munch",
  "logo": {
    "@type": "ImageObject",
    "url": "https://cdn.munch.app/eyJidWNrZXQiOiJtaDQiLCJrZXkiOiJsb2dvXzMwMC5wbmcifQ=="
  }
}

function parseImage(image) {
  if (typeof image === "string") {
    return image
  } else if (image?.sizes && image.sizes['640x640']) {
    return image.sizes['640x640']
  }
}

function parseDate(millis) {
  if (millis) {
    return dateformat(millis, 'yyyy-mm-dd')
  }
}

/**
 * Populate the root head node
 */
function root({title}) {
  if (title) {
    return {title}
  }
  return {}
}

function link({canonical}) {
  const links = []
  if (canonical) {
    links.push({rel: 'canonical', href: canonical})
  }
  return links
}

function meta({robots, title, description, image, url, type}) {
  const meta = []

  if (robots) {
    const {follow, index} = robots
    meta.push({name: 'robots', content: `${follow ? 'follow' : 'nofollow'},${index ? 'index' : 'noindex'}`})
  }

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
  image = parseImage(image)
  if (image) {
    meta.push({hid: 'og:image', name: 'og:image', content: image})
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

function script({breadcrumbs, image, schemaType, headline, updatedAt, publishedAt, authorName}) {
  const script = []

  if (breadcrumbs) {
    script.push({
      type: 'application/ld+json',
      innerHTML: JSON.stringify({
          "@context": "http://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": breadcrumbs.map((obj, index) => ({
            "@type": "ListItem",
            "position": index + 1,
            "name": obj.name,
            "item": obj.item
          }))
        }
      )
    })
  }

  if (schemaType === 'Article') {
    image = parseImage(image)

    script.push({
      type: 'application/ld+json',
      innerHTML: JSON.stringify({
          "@context": 'http://schema.org',
          "@type": 'Article',
          "author": authorName,
          "dateModified": parseDate(updatedAt),
          "datePublished": parseDate(publishedAt),
          "headline": headline,
          "image": image ? [image] : [],
          "publisher": publisher,
        }
      )
    })
  }

  return script
}

export default (context, inject) => {

  /**
   * {
   *    title, description, image, url, type,
   *    schemaType, headline, updatedAt, publishedAt, authorName
   *    robots, canonical, breadcrumbs,
   * }
   */
  inject('head', (properties) => {
    return {
      ...root(properties),
      link: link(properties),
      meta: meta(properties),
      script: script(properties),
      __dangerouslyDisableSanitizers: ['script'],
    }
  })
}
