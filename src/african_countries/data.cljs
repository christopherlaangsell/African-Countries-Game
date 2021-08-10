(ns african-countries.data
  (:require ["@babajidemm/african-countries-api" :as africanCountriesAPI]))



(defn getCountryByName [country]
  (let [info (africanCountriesAPI/byName "Algeria")
        ;; (.. africanCountriesAPI (byName "Algeria") -body toString)
        ]
    (js/console.log info)))

(def fake-countries
  '("Toga"
   "North Africa"
   "Fred"
   "Ligeria"
   "the Democratic People's Republic of Zidan"
   "Gerbal"
   "Brune Ongal"
   "Nwabudike"
   "West Sudan"
   "North Sudan"
   "East Sudan"
   "West Africa"
   "East Africa"
   "Harambe"
   "Jelani"
   "Mansa Islands"
   "Jaali"
   "Sonoca"
   "Mawi"
   "Tanzia"
   "Melizia"
   "Chibuike"
   "Azaan"
   "Chike"
   "Kani Islands"
   "Jaja Islands"
   "Bongo"
   "Angela"
   "Zamboni"
   "West Ethiopia"
   "Gonga"
   "Sunai"
   "the African Confederation"
   "Afrinia"
   "Afromacoland"
   "Azania"
   "Abuddin"
   "Free Republic of Aburiria"
   "Barbar's Kingdom"
   "Bahari"
   "Balaika"
   "Balic"
   "Bangalia"
   "Bapetikosweti"
   "Beninia"
   "Birani"
   "Bialya"
   "Bocamo"
   "Bonande"
   "Bongo Congo"
   "Bora-Baru"
   "Botswanga"
   "Bulmeria"
   "Bulungi"
   "Buranda"
   "Burunda"
   "Butua"
   "Carbombya"
   "Canan"
   "Claw Island"
   "Federal Republic of Darrar"
   "Nort Darrar"
   "Equatorial Kundu"
   "Equatorial Sudan"
   "Federation of Rebellious States"
   "Genosha"
   "Ghalea"
   "Ghudaza"
   "Gindra"
   "Gorilla Nation"
   "Gorotoland"
   "Great Islam Nation"
   "Guadec"
   "Gwinalia"
   "Halwan"
   "Imaya"
   "Interzone"
   "Ishmaelia"
   "Kalubya"
   "Kalya"
   "Kamanga"
   "Kambawe"
   "Kambezi"
   "Kangan"
   "Katanga"
   "Kenyopia"
   "Kharun"
   "Khokarsa"
   "Kijuju"
   "Kinjanja"
   "Kivukiland"
   "Kor"
   "Kukuanaland"
   "Kush"
   "Ligeria"
   "Logosia"
   "Lombuanda"
   "Lyrobia"
   "Lamumba"
   "Malagawi"
   "Matobo"
   "Maurania"
   "Mbangawi"
   "Mittelafrika"
   "Mohannda"
   "Moloni Republic"
   "Mombaka"
   "Mumbambu"
   "Murkatesh"
   "Nadua"
   "Nagonia"
   "Nambabwe"
   "Nambia"
   "Nambutu"
   "Naruba"
   "Narubu"
   "Narobia"
   "Natumbe"
   "Nayak"
   "Neranga"
   "New Zanzibar"
   "Nexdoria"
   "Ng'ombwana"
   "Ngombia"
   "Ng'ambe"
   "Niberia"
   "Nibia"
   "Niganda"
   "Numbani"
   "Nyala"
   "Odan"
   "Opar"
   "Galzburg"
   "Podoso"
   "Ponduka"
   "the Republic of Alabanda"
   "Rudyarda"
   "the Sahelise Republic"
   "Shakobi"
   "Samgola"
   "Sangala"
   "Sonzola"
   "Sotho smietanka"
   "Suaoriland"
   "Talgalla"
   "Tanzaberia"
   "Transvalia"
   "Trucial Abysmia"
   "Ujanka"
   "Umbazi"
   "the National Republic of Umbutu"
   "the United Mitanni Commonwealth"
   "the United States of Southern Africa"
   "Uacma"
   "Wadiya"
   "Wakanda"
   "West Angola"
   "the West African Union"
   "West Monrassa"
   "Zambakia"
   "Zambawi"
   "Zambezi"
   "Zambesi"
   "Zamunda"
   "Zangaro"
   "Zanj"
   "Zanzarim"
   "Zarakal"
   "Zembala"
   "Zinariya"
   "Zulabwe"
   "Zu-Vendis"
   "Zwartheid"
   ))

(def african-countries
  '(
    {:pop 206139589 :name "Nigeria"}
    {:pop 109224414 :name "Ethiopia"}
    {:pop 101334404 :name "Egypt"}
    {:pop 102561403 :name "the Democratic Republic of the Congo"}
    {:pop 59956820 :name "South Africa"}
    {:pop 59734218 :name "Tanzania"}
    {:pop 47564290 :name "Kenya"}
    {:pop 43000420 :name "Algeria"}
    {:pop 45741007 :name "Uganda"}
    {:pop 42268269 :name "Sudan"}
    {:pop 37034729 :name "Morocco"}
    {:pop 31072940 :name "Ghana"}
    {:pop 28013000 :name "Mozambique"}
    {:pop 22671331 :name "Ivory Coast"}
    {:pop 22434363 :name "Madagascar"}
    {:pop 24383301 :name "Angola"}
    {:pop 28524175 :name "Cameroon"}
    {:pop 17138707 :name "Niger"}
    {:pop 18450494 :name "Burkina Faso"}
    {:pop 14528662 :name "Mali"}
    {:pop 16832900 :name "Malawi"}
    {:pop 15473905 :name "Zambia"}
    {:pop 14354690 :name "Senegal"}
    {:pop 13061239 :name "Zimbabwe"}
    {:pop 11039873 :name "Chad"}
    {:pop 10628972 :name "Guinea"}
    {:pop 10982754 :name "Tunisia"}
    {:pop 10515973 :name "Rwanda"}
    {:pop 8260490 :name "South Sudan"}
    {:pop 10008749 :name "Benin"}
    {:pop 15893222 :name "Somalia"}
    {:pop 9823828 :name "Burundi"}
    {:pop 6191155 :name "Togo"}
    {:pop 5298152 :name "Libya"}
    {:pop 6348350 :name "Sierra Leone"}
    {:pop 3859139 :name "the Central African Republic"}
    {:pop 6536000 :name "Eritrea"}
    {:pop 3697490 :name "the Republic of the Congo"}
    {:pop 3476608 :name "Liberia"}
    {:pop 3718678 :name "Mauritania"}
    {:pop 1802278 :name "Gabon"}
    {:pop 2280700 :name "Namibia"}
    {:pop 2024904 :name "Botswana"}
    {:pop 2007201 :name "Lesotho"}
    {:pop 1222442 :name "Equatorial Guinea"}
    {:pop 1882450 :name "Gambia"}
    {:pop 1530673 :name "Guinea-Bissau"}
    {:pop 1261208 :name "Mauritius"}
    {:pop 1119375 :name "Eswatini"}
    {:pop 864618 :name "Djibouti"}
    {:pop 806200 :name "Comoros"}
    {:pop 491875 :name "Cape Verde"}
    {:pop 201784 :name "Sao Tome and Principe"}
    {:pop 90945 :name "Seychelles"}))
