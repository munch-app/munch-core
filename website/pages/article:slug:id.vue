<template>
  <div/>
</template>

<script>
  export default {
    asyncData({$api, redirect, params: {id}}) {
      return $api.get(`/articles/${id}`)
        .then(({data: {id, slug, profile: {username}}}) => {
          return redirect({status: 302, path: `/@${username}/${slug}-${id}`})
        })
    }
  }
</script>
