# spring-thymeleaf-demo
CP353002 Lab - Custom ViewResolver

นางสาวณัฐนันทน์ บุษดี รหัสนักศึกษา 673380037-1 Section 1

# Lab 06 : Custom ViewResolver ใน Spring Boot + Thymeleaf

## จุดประสงค์ของแล็บ

แล็บนี้เป็นการศึกษาการทำงานของ **Custom ViewResolver** ใน Spring Boot ที่ใช้ **Thymeleaf** เป็น View Engine โดยกำหนดให้ ViewResolver ไม่ใช้โฟลเดอร์ `templates` ตามค่าเริ่มต้นของ Spring Boot แต่เปลี่ยนไปใช้โฟลเดอร์ `custom-templates` แทน

นอกจากนี้ยังได้เรียนรู้หลักการทำงานของ MVC (Model - View - Controller) และบทบาทของ ViewResolver ในการแปลงชื่อ View ที่ Controller ส่งกลับ ไปเป็นไฟล์ HTML จริง

---

## สิ่งที่ได้ทำในแล็บ

- สร้างโปรเจกต์ Spring Boot ด้วย Maven
- เพิ่ม Dependency ของ Spring Web และ Thymeleaf
- สร้าง Custom ViewResolver
- เปลี่ยนตำแหน่งของ Template จาก `templates` เป็น `custom-templates`
- สร้าง HomeController สำหรับรับ Request
- ส่งข้อมูลจาก Controller ไปยัง View ผ่าน Model
- แสดงข้อมูลด้วย Thymeleaf
- ทดสอบการทำงานของระบบผ่านเว็บเบราว์เซอร์
- อัปโหลดโปรเจกต์ขึ้น GitHub

---

## โครงสร้างโปรเจกต์

```
spring-thymeleaf-demo
│
├── src
│   └── main
│       ├── java
│       │   └── com.example.demo
│       │       ├── DemoApplication.java
│       │       ├── config
│       │       │     └── ThymeleafConfig.java
│       │       └── controller
│       │             └── HomeController.java
│       │
│       └── resources
│             ├── application.properties
│             └── my-templates
│                   ├── home.html
│                   └── about.html
│
├── pom.xml
└── README.md
```

---

## การทำงานของโปรแกรม

1. ผู้ใช้เปิด Browser
2. Browser ส่ง Request ไปยัง Spring Boot
3. DispatcherServlet ส่ง Request ไปยัง Controller
4. Controller ประมวลผลข้อมูล
5. Controller ส่งชื่อ View เช่น `"home"`
6. Custom ViewResolver นำชื่อ View ไปค้นหาไฟล์ใน `my-templates`
7. Thymeleaf สร้างหน้า HTML
8. ส่งผลลัพธ์กลับไปยัง Browser

---

# การทดลองเพิ่มเติม (ข้อ 14)

## ข้อ 14.1 เพิ่มหน้า About

### สิ่งที่ทำ

- เพิ่ม `@GetMapping("/about")`
- สร้างไฟล์ `about.html`

เมื่อเข้า

```
http://localhost:8080/about
```

หรือ

```
http://localhost:9090/about
```

สามารถเปิดหน้า About ได้ตามปกติ

### ผลการทดลอง

การคืนค่า

```java
return "about";
```

ไม่ใช่การระบุ Path ของไฟล์ HTML โดยตรง แต่เป็นการส่งชื่อ View ให้ ViewResolver ทำหน้าที่ค้นหาไฟล์ `about.html` ภายในโฟลเดอร์ `my-templates` อัตโนมัติ

---

## ข้อ 14.2 ทดลองสร้าง ViewResolver ตัวที่สอง

### สิ่งที่ทำ

สร้าง ViewResolver เพิ่มอีกตัว โดยกำหนด

- ViewResolver ตัวแรก

```
Order = 1
```

ชี้ไปยัง

```
my-templates
```

- ViewResolver ตัวที่สอง

```
Order = 2
```

ชี้ไปยัง

```
templates
```

### ผลการทดลอง

Spring จะเลือกใช้ ViewResolver ที่มีค่า

```
order = 1
```

ก่อนเสมอ

หากค้นหา Template จาก ViewResolver ตัวแรกไม่พบ จึงจะลองใช้ ViewResolver ตัวถัดไป

### สรุป

ค่า `setOrder()` ใช้กำหนดลำดับความสำคัญของ ViewResolver โดยค่าที่น้อยกว่าจะถูกเรียกใช้งานก่อน

---

## ข้อ 14.3 ทดลองเปลี่ยน Suffix

### สิ่งที่ทำ

เปลี่ยน

```java
resolver.setSuffix(".html");
```

เป็น

```java
resolver.setSuffix(".htm");
```

### ผลการทดลอง

เมื่อรันโปรแกรมแล้วเปิดหน้าเว็บ ระบบเกิดข้อผิดพลาด

```
HTTP Status 500
```

หรือ

```
TemplateInputException
```

เนื่องจาก ViewResolver จะพยายามค้นหาไฟล์

```
home.htm
```

แต่ภายในโฟลเดอร์มีเพียง

```
home.html
```

จึงไม่สามารถหาไฟล์ Template ได้

### สรุป

ViewResolver จะสร้าง Path ของไฟล์จาก

```
Prefix + View Name + Suffix
```

ดังนั้นหากกำหนด Prefix หรือ Suffix ไม่ตรงกับไฟล์จริง ระบบจะไม่สามารถหา Template ได้ และเกิด Error
