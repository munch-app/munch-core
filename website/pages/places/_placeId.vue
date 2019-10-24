<template>
  <div/>
</template>

<script>
  export default {
    /**
     * Deprecated, only used for redirecting only
     */
    asyncData({$api, redirect, params: {placeId}}) {
      if (placeId.length === 13) {
        return redirect({status: 301, path: `/${placeId}`})
      }

      return $api.get(`/places-cid/${placeId}`)
        .then(({data: {id, slug}}) => {
          return redirect({status: 301, path: `/${slug}-${id}`})
        })
    }
  }
</script>
