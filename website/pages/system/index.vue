<template>
  <div class="container pt-48 pb-128">
    <div>
      <div v-for="group in groups" :key="group.name">
        <h1>{{group.name}}</h1>

        <div class="mt-24 mb-80">
          <div class="Features flex-1-2-3-4 flex-align-stretch">
            <div class="flex-self-stretch p-12 text-decoration-none" v-for="feature in group.features"
                 :key="feature.path">
              <nuxt-link class="bg-steam black block wh-100 border-3 overflow-hidden" :to="feature.path">
                <div class="hover-bg-a10 bg-fog p-16-24-20">
                  <h3>{{feature.name}}</h3>
                  <div class="mt-4">{{feature.description}}</div>
                </div>
              </nuxt-link>
            </div>
          </div>
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
        groups: [
          {
            name: 'Partner Management',
            features: [
              {
                name: 'Profile',
                description: 'Partner profile hand holding process.',
                path: '/system/profiles',
              },
              {
                name: 'Publication',
                description: 'Articles grouped into a publication.',
                path: '/system/publications',
              }
            ],
          },
          // {
          //   name: 'Data Management',
          //   features: [
          //     {
          //       name: 'Affiliate',
          //       description: 'Affiliate linking management.',
          //       path: '/system/affiliates',
          //     },
          //   ]
          // }
        ],
      }
    },
  }
</script>

<style scoped lang="less">
  .Features {
    margin: -12px;
  }
</style>
