<template>
  <div class="container pt-48 pb-128">
    <div>
      <h1>Available Features</h1>
    </div>

    <div class="mt-32">
      <div class="h-100 Features flex-1-2-3-4 flex-align-stretch">
        <div class="flex-self-stretch p-12 text-decoration-none" v-for="feature in features" :key="feature.path">
          <nuxt-link class="block wh-100 p-16-24-20 border-3" :to="feature.path" :class="feature.classes">
            <h3>{{feature.name}}</h3>
            <div>{{feature.description}}</div>
          </nuxt-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    layout: 'system',
    data() {
      return {
        features: [
          {
            name: 'Profile',
            description: 'Partner profile hand holding process.',
            path: '/system/profiles',
            classes: 'bg-green black',
          },
          {
            name: 'Publication',
            description: 'Articles grouped into a publication.',
            path: '/system/publications',
            classes: 'bg-pink white',
          }
        ]
      }
    },
  }
</script>

<style scoped lang="less">
  .Features {
    margin: -12px;
  }
</style>
