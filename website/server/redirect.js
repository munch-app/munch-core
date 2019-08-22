// Google's John Mueller said 1 year
const redirects = {
  "/contents/ilXAFWw8IRatnNOlEWaP5k/serene-serenetomato": "/@serenetomato/serene-serenetomato-56epv3wgvba01",
  "/contents/azyMMstKG7a8.brRXs25bF/bib-gourmand-2018": "/@munch/bib-gourmand-2018-a30kn446358g1",
  "/contents/U8mG99BVH9y7E57PymKzLV/top-10-restaurants-with-a-view-in-singapore": "/@munch/top-10-restaurants-with-a-view-in-singapore-2cmgrthbh6r01",
  "/contents/W5fCvuebHKyMszS1_KW1W./pancakes": "/@munch/pancakes-rw72vr373ts01",
  "/contents/r1D1uoZ_GBa5oOOznzJ_pF/best-brunch-in-singapore--the-widely-debated-series": "/@munch/best-brunch-in-singapore--the-widely-debated-series-pe0phatxr9j01",
  "/contents/kHA93EdPHr5kacP.u1Px.F/bars-you-should-go-to-if-you-want-to-practise-your-ang-mo-accent": "/@munch/bars-you-should-go-to-if-you-want-to-practise-your-ang-mo-accent-yry4x7chgvyg1",
  "/contents/haJwpocuGQyZDxgi98m3t./healthy-food-and-salads-at-serangoon-nex": "/@munch/healthy-food-and-salads-at-serangoon-nex-pw9x3bn7h0sg1",
  "/contents/jip5csubFFHizIexjSxZ2F/the-best-liu-sha-bao-golden-custard-bun-in-singapore": "/@munch/the-best-liu-sha-bao-golden-custard-bun-in-singapore-b9966ydas8wg1",
  "/contents/AjwC0Zl_ISq3PAg5JLR26k/where-to-sip-coffee-and-stare-out-the-window": "/@munch/where-to-sip-coffee-and-stare-out-the-window-n719fwrrtywg1",
  "/contents/CwML_9vqGSDbPYRQby4dJF/the-straits-times-best-asian-restaurants-silver-award-2018": "/@munch/the-straits-times-best-asian-restaurants-silver-award-2018-ekps6ywa55ng1",
  "/contents/.77O5F4BHb1wgF57hfjK1V/the-ultimate-cheat-day-guide-for-fitspos": "/@munch/the-ultimate-cheat-day-guide-for-fitspos-79z9vxvtyzsg1",
  "/contents/g_N3CranHHyO1qs3fuBPk./ice-cream": "/@munch/ice-cream-as2hdepnag401",
  "/contents/IQj9GlsXIOXNiJoAx9ZXTV/hilary-sgnomster": "/@sgnomster/hilary-sgnomster-b943fw6gkzqg1",
  "/contents/fZfQRb12FFu8gvTkdIGqPk/top-30-pet-friendly-eateries": "/@munch/top-30-pet-friendly-eateries-mdyqyf6z9nz01",
  "/contents/mP.t0fQuIAPAYgkrbQydg./two-stars-award-singapores-top-restaurants-201819": "/@munch/two-stars-award-singapores-top-restaurants-201819-yqx65ttk1h1g1",
  "/contents/7FpSAg8yHXm2zvmiYXfeik/burgers": "/@munch/burgers-ccnn6paqsas01",
  "/contents/ef3l.bycFRLy.wcZwOShGV/two-michelin-stars-2018": "/@munch/two-michelin-stars-2018-96r1dcwtzkm01",
  "/contents/kF83rS8yHKapSBDEZOxs2F/old-school-retro": "/@munch/old-school-retro-arwweabxfrs01",
  "/contents/NabTeth8IDTkwP6ZiG5LM./bishan": "/@munch/bishan-338eqprtj9p01",
  "/contents/XVqC3oLXInmfqltFGrBQi./cookies": "/@munch/cookies-25zvq36yy6p01",
  "/contents/EpeIrCeIFLiBy3fQtSD.2k/daniel-foodchiak": "/@foodchiak/daniel-foodchiak-8xbnsbqw27pg1",
  "/contents/307KJ_qzHy9GSglNDFMJ0F/one-star-award-singapores-top-restaurants-201819": "/@munch/one-star-award-singapores-top-restaurants-201819-4n2vvsbqwsp01",
  "/contents/nYxMJh_1F2DL0EkOUFNqkk/top-10-seafood-restaurants-in-singapore": "/@munch/top-10-seafood-restaurants-in-singapore-nyhneq710b4g1",
  "/contents/5ZTpXilbIh1GBYqgcPhCyF/randy-lim-randylim27": "/@randylim27/randy-lim-randylim27-pft7m310b2301",
  "/contents/BUPxlk3eHHeK_AlbgqalEF/top-10-japanese-restaurants-in-singapore": "/@munch/top-10-japanese-restaurants-in-singapore-z7fwwg11a7eg1",
  "/contents/cSMxlin4GJ5pZDbaO08FRk/old-airport-road-food-centre": "/@munch/old-airport-road-food-centre-dd0kdrefswy01",
  "/contents/vGx7A3lWFQeCMbwbAQiJ_V/cathryn-lee-msoinkee": "/@msoinkee/cathryn-lee-msoinkee-q2czh242kn801",
  "/contents/YnYC3Gw_F3DrCvqd.9OwEk/4-halal-affordable-meals-and-deals-in-serangoon": "/@munch/4-halal-affordable-meals-and-deals-in-serangoon-hd0wytm6e5dg1",
  "/contents/NcMoLulpFrqFy8LnnjS1AF/anchelski-nusfatclub": "/@nusfatclub/anchelski-nusfatclub-92a0b7g55ggg1",
  "/contents/pCVf0HkPHAmLsaKnNejTgF/keane--hilary-ahgongahmajiak": "/@ahgongahmajiak/keane--hilary-ahgongahmajiak-nk4296c5c0r01",
  "/contents/VqJvtv33IFeDfOdldh_RvF/qing-xiang-qingxiangsqx": "/@qingxiangsqx/qing-xiang-qingxiangsqx-6wff4ag1csg01",
  "/contents/dpAuKvQvF.9kGlDNmNLUfV/golden-mile-food-centre": "/@munch/golden-mile-food-centre-hdzvxq30fd0g1",
  "/contents/CEGzvYI7FXLx_fHjQbXN0F/fried-chicken": "/@munch/fried-chicken-wmd1psr0ybn01",
  "/contents/znRgEzWfHhXY2Ug8e.8gdF/navigating-through-restaurants-in-nex-mall-serangoon": "/@munch/navigating-through-restaurants-in-nex-mall-serangoon-k37811m9zsjg1",
  "/contents/XPdp22CRG4XvfSRu6sfxEk/pasta": "/@munch/pasta-hy4fj3smsxj01",
  "/contents/0a5__l_NGPTrwFyuVEct.V/top-10-authentic-thai-restaurants-in-singapore": "/@munch/top-10-authentic-thai-restaurants-in-singapore-x05qsnqvf0kg1",
  "/contents/qd__XRsEGfupOSJ_CKeeE./dim-sum": "/@munch/dim-sum-z3996x1wavp01",
  "/contents/vcDk1k_tH6LwhkpKn.uVTk/top-10-places-to-eat-in-tanjong-pagar": "/@munch/top-10-places-to-eat-in-tanjong-pagar-yq8899b57vjg1",
  "/contents/EypmCu7FFViDE2g3Yisi4./calvin-lee-foodmakescalhappy": "/@foodmakescalhappy/calvin-lee-foodmakescalhappy-9qxrh8q3bby01",
  "/contents/pFAhH.pPIZm9RKk2QluBhV/diana-tan-dianaaatan": "/@dianaaatan/diana-tan-dianaaatan-rgmyn78yedtg1",
  "/contents/FF9nEFnjISLBi1i2JI9QjV/sashimi": "/@munch/sashimi-y9s1y5zknpm01",
  "/contents/GlNaw.i5IzeTor0EUD1Gp./local-desserts": "/@munch/local-desserts-5fh0y3vd2t2g1",
  "/contents/JVpKXTjSI4uncWQkVx9uek/three-stars-award-singapores-top-restaurants-201819": "/@munch/three-stars-award-singapores-top-restaurants-201819-qk29ysc5dtv01",
  "/contents/CzLZwbYvGSHN2Y.MOnJKYk/top-10-supper-spots-in-singapore": "/@munch/top-10-supper-spots-in-singapore-ktc1aqfrxahg1",
  "/contents/LNZMikPkGWmNr587Ar8b4./top-10-vegetarian-restaurants-in-singapore": "/@munch/top-10-vegetarian-restaurants-in-singapore-c3ehkdehqwdg1",
  "/contents/zDgvXHocG0DzV8L5A3zJM./top-10-cafes-in-singapore": "/@munch/top-10-cafes-in-singapore-w1w076ns0an01",
  "/contents/f0DicaeJHPX0LLSIn_LVBk/where-to-eat-in-orchard-when-your-bank-account-goes-bruh": "/@munch/where-to-eat-in-orchard-when-your-bank-account-goes-bruh-4weg1nxgpxr01",
  "/contents/wNn59ZHhIjada3fpAIsMtV/ang-mo-kio": "/@munch/ang-mo-kio-rkxe1tapyctg1",
  "/contents/YEvz3GuMHEeJNG26ffW1SF/pizza": "/@munch/pizza-wyqag5b8gz7g1",
  "/contents/kwPQwlfoIMXRad2saybqHk/wine-list-bronze-award-singapores-top-restaurants-201819": "/@munch/wine-list-bronze-award-singapores-top-restaurants-201819-ygee3tr0cf501",
  "/contents/3Gj7vdHRGuXlEwoAP1XHK./top-10-child-friendly-eateries-in-singapore": "/@munch/top-10-child-friendly-eateries-in-singapore-a5dpnredxkqg1",
  "/contents/3OyeBqP2FK1zuVHHiFpjUk/wine-list-silver-award-singapores-top-restaurants-201819": "/@munch/wine-list-silver-award-singapores-top-restaurants-201819-6gnqnc7xhkh01",
  "/contents/noPLaA9RHE5pJnUqlpXB0./10-study-cafes-with-wifi--plugs": "/@munch/10-study-cafes-with-wifi--plugs-jf1kxscj36t01",
  "/contents/x_leSqhlIKurNO2.zazcs./cakes": "/@munch/cakes-jh80cmjgy8m01",
  "/contents/W267jMySIgyQFDqwDopuM./lirong-lirongs": "/@lirongs/lirong-lirongs-s67temf0hfa01",
  "/contents/nJdUX0EaGgXntMcHpP_p7./chicken-rice": "/@munch/chicken-rice-pdhznchtqt501",
  "/contents/0aHM7KNeGu11gz._N.d3nk/sheryl-sherbakes": "/@sherbakes/sheryl-sherbakes-snebetfnf8r01",
  "/contents/FlGEfVecI2HXjkgNlHjxYF/neon-light-cafe-guide": "/@munch/neon-light-cafe-guide-yv969dq987q01",
  "/contents/2gn76Fi_HPmsfXUBI4mwVk/featured-desserts-from-our-lazy-sunday": "/@munch/featured-desserts-from-our-lazy-sunday-yz9qdngv115g1",
  "/contents/_Bxk7xBsHELWu6ZXrGVFHV/burgers-or-bagels": "/@munch/burgers-or-bagels-ns7hz3p9zv001",
  "/contents/dfDUvcb3FV5riCSMZq97UV/where-to-bring-your-date-if-you-want-to-get-lots-of-good-food": "/@munch/where-to-bring-your-date-if-you-want-to-get-lots-of-good-food-64x55vtxm8rg1",
  "/contents/mYaHklf0HrHw8AkrGN13qV/top-10-value-for-money-restaurants-in-singapore": "/@munch/top-10-value-for-money-restaurants-in-singapore-b92cm9vqswfg1",
  "/contents/j.cH_xqOFS9dcFoL7QNZwk/world-gourmet-summit-awards-2018": "/@munch/world-gourmet-summit-awards-2018-r2ce0nf25p4g1",
  "/contents/aUBPUmIQIOaUafDsCb3cTV/top-10-western-restaurants-in-singapore": "/@munch/top-10-western-restaurants-in-singapore-hs6vxvvefy9g1",
  "/contents/ZR_UskzmHUX9Wi05BZyn5F/one-michelin-star-2018": "/@munch/one-michelin-star-2018-597z9r6ymkp01",
  "/contents/rdi7Axm7I5HsJfjUyFYgfk/justin-teo-justinfoodprints": "/@justinfoodprints/justin-teo-justinfoodprints-tcwxqk650e701",
  "/contents/5d73vymIFvXLOPbSRXha3./nikolai-wee-nikolaiwee": "/@nikolaiwee/nikolai-wee-nikolaiwee-7erxymbjvyt01",
  "/contents/D7kzmil.GNLZwP7eWA1hRV/amoy-street-food-centre": "/@munch/amoy-street-food-centre-b4v00hannr9g1",
  "/contents/Fs6YJ9CnHhPUfeHZiph.2k/ramen": "/@munch/ramen-ag1zfw20n9101",
  "/contents/UtxteO25FILW_59DJ005HV/top-10-business-meals-appropriate-restaurants-in-singapore": "/@munch/top-10-business-meals-appropriate-restaurants-in-singapore-wgece98n50g01",
  "/contents/Bsbh64iJFVXwMsOtFtzGvk/nex-halal-top-picks": "/@munch/nex-halal-top-picks-7cncs0gk6y7g1",
  "/contents/cHlpu.TUFFijGpy4OUvNIk/ryan-chen-ryancsggluttony": "/@ryancsggluttony/ryan-chen-ryancsggluttony-dmvwj16959g01",
  "/contents/QXartfjEGKmuCUZ8OJlgak/ghim-moh-market": "/@munch/ghim-moh-market-y5zbf93xtw1g1",
  "/contents/v6jp3XQxF2DaMG71mOQH1F/where-id-go-for-dinner-if-i-won-toto": "/@munch/where-id-go-for-dinner-if-i-won-toto-pnt1yqbc883g1",
  "/contents/vEsXru18FC9N0rNgSxNuYk/kenneth-lee-5meanders": "/@5meanders/kenneth-lee-5meanders-j88jqm2rbjn01",
  "/contents/WPMylzC3F6m1Ztd3ZtA_bV/tiong-bahru-market": "/@munch/tiong-bahru-market-apze4h50qw3g1",
  "/contents/o7XBGVLCHH9tvaWypiw6TV/maxwell-food-centre": "/@munch/maxwell-food-centre-v87rnm239hjg1",
  "/contents/gFwnyRNWF_DMbjI00ekQ9F/munch-eatbetween-function-guide": "/@munch/munch-eatbetween-function-guide-mvfbre716v7g1",
  "/contents/FemKZQSYGdTs689nJPtAtF/the-alter-egos-of-michelin-starred-restaurants": "/@munch/the-alter-egos-of-michelin-starred-restaurants-5cr4kbjvr80g1",
  "/contents/igsjUYUQGKqdpJOIZPVjmF/kenny-tang-boyz86": "/@boyz86/kenny-tang-boyz86-j8n771pk6z5g1",
  "/contents/hi1y6ue.IQ10YTqRtmr_2F/top-10-pet-friendly-restaurants--cafes-in-singapore": "/@munch/top-10-pet-friendly-restaurants--cafes-in-singapore-s40prsmemh5g1",
  "/contents/HFvQwfeoFy9igyjTuoYcjV/wine-list-gold-award-singapores-top-restaurants-201819": "/@munch/wine-list-gold-award-singapores-top-restaurants-201819-6j1m36ae3y201",
  "/contents/8822AqT4IjHowndbbPElK./good-food--childs-play": "/@munch/good-food--childs-play-74f4ct02cf4g1",
  "/contents/bdRI5mcUHCT9.J5J3FwPWk/the-straits-times-best-asian-restaurants-gold-award-2018": "/@munch/the-straits-times-best-asian-restaurants-gold-award-2018-qm7h055jn2h01",
  "/contents/cUFbEZogIY1mLG0dJ5YkVF/alex-lohsqtop": "/@sqtop/alex-lohsqtop-9kj5jp8nres01"
}

module.exports = function (req, res, next) {
  const to = redirects[req.url]
  if (to) {
    res.writeHead(301, {Location: to})
    res.end()
  } else {
    next()
  }
}
