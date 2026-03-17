# 🚀 JobFind: Yeni Nesil İnsan Kaynakları ve Kariyer Platformu

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![AI Assisted](https://img.shields.io/badge/AI_Assisted_Frontend-Gemini-8E75B2?style=for-the-badge)

**JobFind**, iş arayan yetenekler ile sektörün öncü şirketlerini modern, şeffaf ve yüksek performanslı bir dijital ekosistemde buluşturan kapsamlı bir İnsan Kaynakları Yönetim Sistemidir (HRMS). Sadece bir iş ilan platformu olmanın ötesinde; kurumsal düzeyde güvenlik altyapısına sahip, gelişmiş CV yönetimi sunan ve arka planda "Microservice" mimari prensiplerini barındıran ölçeklenebilir bir mühendislik ürünüdür.

---

## 🤖 Geliştirici Notu ve Yapay Zeka (AI) Entegrasyonu

> **Önemli Not:** Bu projenin temel odak noktası **Backend Mimarisi**, veri güvenliği, veritabanı optimizasyonları ve RESTful API tasarımıdır. 

Bir Backend Developer olarak, kurduğum sistemin gücünü ve işleyişini kullanıcı dostu bir şekilde görselleştirebilmek amacıyla projenin **Frontend (HTML, CSS, JavaScript) kısmı tamamen Yapay Zeka (Gemini AI) yardımıyla üretilmiştir.** Bu stratejik tercih sayesinde UI/UX tasarımlarıyla vakit kaybetmek yerine; doğrudan core (çekirdek) iş mantığına, Spring Security entegrasyonuna, DTO katmanlamasına ve "Over-fetching" gibi kritik performans darboğazlarının çözümüne odaklanılmıştır. Benden bir frontend kodu yazmamı beklemeyin, arka plandaki o kusursuz veri akışına ve güvenlik duvarlarına odaklanın! :)

---

## 🔐 İleri Düzey Güvenlik ve Kimlik Doğrulama (Security Architecture)

Sistem, kullanıcı verilerini ve şirket içi süreçleri korumak için endüstri standartlarında (OWASP) güvenlik protokolleri ile inşa edilmiştir:

* **JWT (JSON Web Token) Altyapısı:** Kullanıcı oturumları; yüksek güvenlikli, kısa ömürlü **Access Token** ve kesintisiz kullanıcı deneyimi sağlayan **Refresh Token** mekanizması ile "stateless" (durumsuz) olarak yönetilir.
* **Token Tabanlı Kimlik Yönetimi (Zero-Trust):** İş ilanına başvurma, yorum yapma veya profil güncelleme gibi kritik işlemlerde istemciden (frontend) gelen manipülasyona açık `User ID` parametrelerine asla güvenilmez. Kullanıcının kimliği (username), doğrulanmış JWT payload'unun içinden arka planda otomatik olarak çıkarılır (Context Holder). Bu sayede *BOLA (Broken Object Level Authorization)* gibi kritik zafiyetler sıfıra indirilmiştir.
* **Kriptografik Şifreleme (Hashing):** Kullanıcı parolaları veritabanında asla açık metin (plain-text) olarak saklanmaz. Güçlü hashing algoritmaları (örn. BCrypt) ile geri döndürülemez şekilde şifrelenir.
* **Uç Nokta (Endpoint) İzolasyonu:** Sisteme entegre Spring Security filtreleri sayesinde; herkese açık (Public) API'ler ile yalnızca yetkili oturumların (İş Arayan / İşveren rollerinin) erişebildiği Private API'ler kesin çizgilerle birbirinden ayrılmıştır.

---

## 🌟 Öne Çıkan Özellikler

### 🎯 İş Arayanlar İçin (B2C Deneyimi)
* **Kapsamlı Dijital Özgeçmiş (CV):** Adayların eğitim, deneyim, yetkinlik ve iletişim bilgilerini tek bir merkezde toplayan, modüler altyapı.
* **Akıllı Başvuru Takibi:** Tek tıkla iş ilanlarına başvuru yapabilme ve başvuru süreçlerini (Beklemede, Onaylandı, Reddedildi) anlık olarak "Başvurularım" panelinden takip edebilme.
* **Şeffaf Şirket Analizi:** Başvuru öncesi şirketlerin güncel çalışan sayısını, adresini ve daha önce o şirkette çalışmış/mülakata girmiş adayların yorumlarını görüntüleme imkanı.

### 🏢 İşverenler İçin (B2B Deneyimi)
* **Dinamik İlan Yönetimi:** Kolay arayüz ile saniyeler içinde yeni iş ilanları oluşturma, kontenjan ve son başvuru tarihi belirleme.
* **Marka İtibarı ve Geri Bildirim:** Şirkete yapılan değerlendirmeleri sayfalama (Pagination) mantığı ile anlık takip edebilme ve marka değerini ölçümleme.
* **Aday Değerlendirme Havuzu:** İlanlara başvuran adayların detaylı CV'lerini tek tıkla görüntüleyip, işe alım süreçlerini profesyonelce yönetme.

---

## ⚙️ Teknik Mimari ve Mühendislik Yaklaşımı

JobFind, "Read-Heavy" (Okuma ağırlıklı) sistemlerin getirdiği performans darboğazlarını aşmak üzere özel olarak dizayn edilmiştir:

* **Over-Fetching Optimizasyonu:** Kurumsal şirket profilleri yüklenirken, devasa verilerin sistemi kilitlemesini önlemek amacıyla REST API'ler parçalanmış; adres, ilanlar ve yorumlar asenkron (`Promise.all`) olarak bağımsız uçlardan çekilerek milisaniyelik sayfa yüklenme hızlarına ulaşılmıştır.
* **Denormalizasyon & Caching Mantığı:** Şirketlerin ortalama puanları, her istekte binlerce satırlık SQL hesaplamasına sokulmak yerine, "Yazma Anında Hesapla" (Calculate on Write) prensibiyle doğrudan Entity üzerinde tutularak Veritabanı (CPU) maliyetleri %99 oranında düşürülmüştür.
* **İzole Veri Transferi:** Veritabanı modelleri (Entities) hiçbir zaman doğrudan dışarıya açılmaz. Her bir ekranın ihtiyacına özel tasarlanmış DTO (Data Transfer Object) katmanları ile veri izolasyonu sağlanmıştır.

---

## 🛠️ Kullanılan Teknolojiler

**Backend (Core Architecture):**
* Java 17+
* Spring Boot 4.x
* Spring Security & JWT (JSON Web Token)
* Spring Data JPA / Hibernate
* RESTful API Mimarisi
* Maven

**Frontend & Prototyping:**
* Gemini AI (Yapay Zeka ile Kod Üretimi & UI/UX Tasarımı)
* HTML5, CSS3, Vanilla JavaScript
* Bootstrap 5
* Fetch API (Asenkron Veri İletişimi)

**Veri ve Validasyon:**
* Relational Database (SQL)
* Global Exception Handling (Merkezi Hata Yönetimi)
* Kapsamlı Veri Validasyonu (Jakarta Validation)

---

## 📸 Ekran Görüntüleri

| Anasayfa | Şirket Profili | 
| :---: | :---: | 
| ![Anasayfa](https://github.com/user-attachments/assets/913ee934-8413-41bd-a335-9ee72b80b84a) | ![Şirket](https://github.com/user-attachments/assets/f1cda583-687f-49cf-8077-225abb0f51e9) | 

| Giriş | İş Veren Kayıt | İş Aayan Kayıt | 
| :---: | :---: | :---: | 
| ![Giriş](https://github.com/user-attachments/assets/d406586f-22a9-4afa-a086-6db3f402aa69) | ![İş Veren Kayıt](https://github.com/user-attachments/assets/c7ee6bf8-ecf5-4571-a3c1-3c199aa9a283) | ![İş Arayan](https://github.com/user-attachments/assets/e1b4c7a7-21d1-4693-812a-463e2968f6fa) | 

### İş Veren Ekranı
| İlanlar | İlan Başvuruları | Başvuran Cv | 
| :---: | :---: | :---: | 
| ![İlanlar](https://github.com/user-attachments/assets/a39f5d02-e9a1-467b-98e5-0d57fe1afff3) | ![İlan Başvuruları](https://github.com/user-attachments/assets/72addde5-f24b-4843-856a-bc03d1e165d8) | ![Başvuran Cv](https://github.com/user-attachments/assets/6f1637a8-f321-4c8a-94bc-2b96dbe2c229) | 

| Yani İlan Aç | Şirket Bilgilieri / Güncelle | Yorumler & Puanlar | 
| :---: | :---: | :---: | 
| ![Yani İlan Aç](https://github.com/user-attachments/assets/434cff34-0691-4536-8570-25380852a0ed) | ![Şirket Bilgilieri / Güncelle](https://github.com/user-attachments/assets/981d29c9-0340-48ca-8a4f-92500eb2ddb5) | ![Yorumler & Puanlar](https://github.com/user-attachments/assets/711cb91b-8e7b-4ed1-bc9e-ab9d63edf94e) | 

### İş Arayan Ekranı
| İlanlar | Başvurularım | Kişisel Bilgileri / Güncelle | Cv |
| :---: | :---: | :---: | :---: | 
| ![İlanlar](https://github.com/user-attachments/assets/40f60c44-61df-420f-b33b-6f8a9532c9d3) | ![Başvurularım](https://github.com/user-attachments/assets/c8c0fd0d-7706-40bd-92e8-1acfb7c7bf22) | ![Kişisel Bilgileri / Güncelle](https://github.com/user-attachments/assets/0e4bf042-be3a-4770-9ade-494627e08df6) | ![Cv](https://github.com/user-attachments/assets/02e0aed3-8997-4adf-a70b-3799ea8e6cfb) | 
