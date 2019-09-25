<template>
  <div class="container pt-48 pb-64">
    <h1>
      <span v-if="error.statusCode === 404">
        {{error.message || 'Page Not Found'}}
      </span>
      <span v-else>
        {{error.message}}
      </span>
    </h1>

    <div class="mt-16">
      <nuxt-link to="/">
        <button class="blue">Go Home</button>
      </nuxt-link>
    </div>
  </div>
</template>

<script>
  export default {
    props: ['error']
  }
</script>
