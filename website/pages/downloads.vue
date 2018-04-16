<template>
  <div class="Downloads">
    <div class="OverlayBanner">
      <section class="Banner">
        <b-container>
          <b-row style="text-align: left">
            <b-col md="5" offset-md="1" cols="12">
              <h1 style="margin-top: 30px">DISCOVER DELICIOUS</h1>
              <h6 style="margin-top: 30px; font-size: 120%">
                Whether you're looking for the perfect date spot or the hottest bar in town - Munch helps you answer the
                question:
              </h6>
              <h6 style="margin-top: 30px; font-size: 120%">
                'What do you want to eat?'
              </h6>
              <div style="margin-top: 30px">
                <a href="https://itunes.apple.com/sg/app/munch-food-discovery/id1255436754?ls=1&mt=8"
                   rel="noopener noreferrer">
                  <b-img style="height: 50px" src="/img/downloads/app_store.svg"/>
                </a>
              </div>
            </b-col>
            <b-col md="4" cols="12" class="d-none d-md-block">
              <b-img right style="max-height: 500px;" src="/img/downloads/munch_app.png"/>
            </b-col>
          </b-row>
        </b-container>
      </section>
    </div>

    <section class="About container">
      <b-container>
        <b-row>
          <b-col offset-md="1" md="5">
            <h3 style="text-align: right">WHY WE DO WHAT WE DO</h3>
            <p style="text-align: right">Our mission here at Munch is to help answer a simple question that plagues us
              all - <b>what do you want to eat?</b></p>
          </b-col>
          <b-col md="4">
            <p>We were tired of the endless Google searches, pestering friends (who take forever to reply) and walking
              around
              without a clue trying to find the right spot to eat. It's ironic because there's a ton of food
              information, in
              fact too much for a human to digest (hah). <b>Nobody</b> wants to go through a list of 27 places I MUST
              try in
              Marina
              Bay while standing in Ang Mo Kio.</p>

            <p>We created Munch to do one thing - tell me what I should be stuffing in my face.</p>

            <p>And by 'my face' we truly do mean each user. Munch is powered by our in-house engine that uses artificial
              intelligence and machine learning to show you what's delicious. That means the more you tell us what you
              like,
              the better the suggestions become.</p>

            <p>With Munch, you'll never have to eat 'Anything' ever again.</p>
          </b-col>
        </b-row>
      </b-container>


    </section>

    <div class="OverlayFeatures">
      <section class="Features container text-center">
        <h3>FEATURES</h3>
        <h6>Finding a place to eat has never been this simple.</h6>

        <b-container>
          <b-row>
            <b-col cols="12" md="4" class="Content d-flex align-items-stretch">
              <b-card title="Save Your Spot" img-src="/img/downloads/icons8-open.svg" img-top>
                <p class="card-text">
                  Save places that you want to check out or create themed lists to keep track of places.
                </p>
              </b-card>
            </b-col>
            <b-col cols="12" md="4" class="Content d-flex align-items-stretch">
              <b-card title="True Discovery" img-src="/img/downloads/icons8-compass.svg" img-top>
                <p class="card-text">
                  Explore thousands of restaurants, bars and hawkers in the app. Find places nearby or on the other end
                  of
                  the island.
                </p>
              </b-card>
            </b-col>
            <b-col cols="12" md="4" class="Content d-flex align-items-stretch">
              <b-card title="Good or Not" img-src="/img/downloads/icons8-news.svg" img-top>
                <p class="card-text">
                  Read the latest articles from bloggers such as MissTamChiak & Eatbook.
                </p>
              </b-card>
            </b-col>
          </b-row>

          <b-row>
            <b-col cols="12" md="4" offset-md="2" class="Content d-flex align-items-stretch">
              <b-card title="We Eat With You*" img-src="/img/downloads/icons8-mind-map.svg" img-top>
                <p style="font-size: 75%"><b>*This feature is in Beta mode and will be released to the public soon.</b>
                </p>
                <p>
                  Munch learns what you like and improves the suggestions over time.
                </p>
              </b-card>
            </b-col>
            <b-col cols="12" md="4" class="Content d-flex align-items-stretch">
              <b-card title="Tell the world*" img-src="/img/downloads/icons8-very-popular-topic.svg" img-top>
                <p style="font-size: 75%"><b>*This feature is in Beta mode and will be released to the public at a later
                  date.​</b></p>
                <p class="card-text">
                  Leave reviews and share images of places you visit.
                </p>
              </b-card>
            </b-col>
          </b-row>

        </b-container>
      </section>
    </div>

    <section class="Partners text-center container">
      <h3>INSTAGRAM PARTNERS</h3>
      <h6>Always causing people to drool on their phone? </h6>
      <h6>Share your work on <a target="_blank" href="http://partner.munchapp.co">partner.munchapp.co</a></h6>

      <h6 style="margin-top: 50px; margin-bottom: 25px">Some of the mouth-watering images from our partner
        community.</h6>

      <partner-media-grid :medias="partner_12_media"/>
    </section>

    <div class="OverlayContact">

      <section class="Contact container">
        <b-row>
          <b-col md="6" class="d-none d-md-block">
            <b-img src="/img/downloads/contact.jpg" fluid/>
          </b-col>
          <b-col offset-md="1" md="5">
            <h3 style="text-align: left">GET IN TOUCH</h3>
            <hr>
            <p>Have any questions or want to grab a cup of coffee? Please feel free to contact us at our email address
              below.</p>
            <hr>

            <table>
              <tbody>
              <tr>
                <td class="PlatformTitle">EMAIL:</td>
                <td class="PlatformLink"><a href="mailto:contact@munch.space">contact@munch.space</a></td>
              </tr>
              <tr>
                <td class="PlatformTitle">INSTAGRAM:</td>
                <td class="PlatformLink"><a target="_blank" href="https://www.instagram.com/munch.app"
                                            rel="noopener noreferrer"
                >@munch.app</a>
                </td>
              </tr>
              </tbody>
            </table>
          </b-col>
        </b-row>
      </section>
    </div>
  </div>
</template>

<script>
  import PartnerMediaGrid from '~/components/PartnerMediaGrid.vue'
  import axios from 'axios';

  export default {
    components: {
      PartnerMediaGrid
    },
    async asyncData() {
      let instagramReq = axios.get('https://api.partner.munchapp.co/v1/instagram/public/medias/12')

      let instagramRes = await instagramReq
      return {partner_12_media: instagramRes.data.data}
    }
  }
</script>

<style>
  .Downloads section {
    padding-top: 75px;
    padding-bottom: 75px;
  }

  .Downloads p {
    font-size: 90%;
  }

  .Downloads h3 {
    margin-bottom: 20px;
  }

  .Banner {
    background-color: #00000060;
  }

  .OverlayBanner {
    background: url("/img/downloads/banner.jpg");
    background-position: center center;
    background-size: cover;

    color: white;
  }

  .OverlayFeatures {
    background: #f8f8f8;
  }

  .About p {
    margin-bottom: 20px;
  }

  .Features .Content p, h4 {
    text-align: center;
  }

  .Features .Content img {
    height: 60px;
    width: 60px;
    margin: 30px auto 0 auto;
  }

  .Features .Content {
    margin-top: 30px;
  }

  .OverlayContact {
    background: #f8f8f8;

  }

  .Contact .PlatformTitle {
    font-weight: 600;
    font-size: 80%;
  }

  .Contact .PlatformLink {
    font-size: 85%;
    padding-left: 10px;
  }
</style>
