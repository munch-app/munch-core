<template>
  <div>
    <section class="bg-white">
      <div class="container ptb-48">
        <div class="flex-1-2 m--24">
          <div class="p-24">
            <h1>We believe in creating. <br>Not configuration.</h1>
            <p class="mt-8">
              Become a Munch partner,
              write and create what you desire and we will take care of the rest.
            </p>
            <div class="mt-24">
              <button class="pink" @click="onGetStarted">Get started</button>
            </div>
          </div>
          <div class="p-24">
            <img class="border-3 w-100"
                 src="https://cdn.munch.app/eyJidWNrZXQiOiJtaDQiLCJrZXkiOiJicmVha2Zhc3QtYXQtaG9tZS13b3JrcGxhY2UuanBnIiwiZWRpdHMiOnsicmVzaXplIjp7IndpZHRoIjoxMDgwLCJoZWlnaHQiOjEwODAsImZpdCI6Im91dHNpZGUifX19">
          </div>
        </div>
      </div>
    </section>


    <section class="bg-steam">
      <div class="container ptb-48">
        <div class="flex-column-align-center">
          <div class="border-3 p-6-12 bg-white">
            <h5>Munch Partner Studio</h5>
          </div>
        </div>
        <div class="mt-24">
          <h2 class="mb-24 text-center">You</h2>

          <div class="Creators flex-center flex-1-2-3-4 m--12">
            <div>
              <div>
                <h4>Research</h4>
                <p>A database of all the places information you need.</p>
              </div>
            </div>
            <div>
              <div>
                <h4>Writing</h4>
                <p>Easy, clean and intuitive place to write without the fuzz.</p>
              </div>
            </div>
            <div>
              <div>
                <h4>Social Media</h4>
                <p>Integrated directly with your audience.</p>
              </div>
            </div>
            <div>
              <div>
                <h4>Monetization <span class="tiny-bold b-a60">Coming soon</span></h4>
                <p>You decide when and how you want to get paid.</p>
              </div>
            </div>
          </div>
        </div>

        <div>
          <div class="mtb-32 flex-center">
            <div class="Line bg-black border-2"/>
          </div>
          <h2 class="mb-24 text-center">Munch</h2>

          <div class="Features flex-center flex-wrap m--12">
            <div class="p-8-12" v-for="feature in features" :key="feature.name">
              <div class="h6 p-4-12 bg-white border-3">
                {{feature.name}}
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="bg-white">
      <div class="container ptb-64">
        <div class="Contact flex m--24">
          <div class="Image p-24">
            <img class="w-100 border-3"
                 src="https://cdn.munch.app/eyJidWNrZXQiOiJtaDQiLCJrZXkiOiJ3cml0ZS1hLWxldHRlci10by1hLWxvdmVkLW9uZS5qcGciLCJlZGl0cyI6eyJyZXNpemUiOnsid2lkdGgiOjEwODAsImhlaWdodCI6MTA4MCwiZml0Ijoib3V0c2lkZSJ9fX0="
            >
          </div>
          <div class="flex-grow p-24">
            <h2>Contact us</h2>
            <p>Have any questions or want to grab a cup of coffee?</p>
            <p class="mt-8">Email: <a href="mailto:partner@munch.app">partner@munch.app</a></p>
          </div>
        </div>
      </div>
    </section>

    <header-middle>
      <h4>Partner Program</h4>
    </header-middle>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import HeaderMiddle from "../components/layouts/HeaderMiddle";

  export default {
    components: {HeaderMiddle},
    head() {
      return this.$head({
        robots: {follow: true, index: true},
        canonical: `https://www.munch.app/partner`,
        title: `Partner Program · Munch`,
        url: `https://www.munch.app/partner`,
        breadcrumbs: [
          {
            name: 'Partner',
            item: `https://www.munch.app/partner`
          },
        ]
      })
    },
    data() {
      return {
        features: [
          {name: 'Search Engine Optimization'},
          {name: 'Metadata Building'},
          {name: 'Schema.org Linked Data'},
          {name: 'Open Graph Protocol'},
          {name: 'Instagram Integration'},
          {name: 'Mobile Optimization'},
          {name: 'Mobile Application'},
          {name: 'WYSIWYG Editor'},
          {name: 'Server Provisioning'},
          {name: 'Server Scaling'},
          {name: 'Content Publications'},
          {name: 'Content Publishing'},
          {name: 'Content Delivery'},
        ]
      }
    },
    computed: {
      ...mapGetters('account', ['isLoggedIn']),
    },
    methods: {
      onGetStarted() {
        if (this.isLoggedIn) {
          this.$router.push({path: `/me/articles`})
        } else {
          this.$store.commit('global/setDialog', {name: 'GetStartedDialog', props: {defaultState: 'sign-up'}})
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .Creators {
    > div {
      padding: 12px;
      align-self: stretch;

      > div {
        padding: 16px;
        border: 3px;
        background: #FFF;
        height: 100%;
      }
    }
  }

  .Line {
    width: 2px;
    height: 100px;
  }

  .Features {
    @media (min-width: 768px) {
      padding-left: 32px;
      padding-right: 32px;
    }

    @media (min-width: 1080px) {
      padding-left: 64px;
      padding-right: 64px;
    }
  }

  .Contact {
    .Image {
      @media (max-width: 768px) {
        display: none;
      }

      @media (min-width: 768px) {
        flex: 0 0 33.33333%;
        max-width: 33.33333%;
      }
    }
  }
</style>
