CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO "order".payment_outbox(
	id, saga_id, created_at, processed_at, type, payload, outbox_status, saga_status, order_status, version)
	VALUES (uuid_generate_v4(), uuid_generate_v4(), now(), now(),'OrderProcessingSaga', '{
  "id": "412690f0-4ee0-4c2d-882c-8a8bedc4a1d6",
  "price": 1,
  "sagaId": "88cf56af-851c-4756-8c59-65152b01348a",
  "orderId": "25694e6f-2a41-4bf3-bd8f-a5a84a21405d",
  "createdAt": "2023-06-14T23:42:00.976896Z",
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41",
  "paymentOrderStatus": "PENDING"
}', 'STARTED', 'STARTED', 'PENDING', 0);



INSERT INTO "order".restaurant_approval_outbox(
	id, saga_id, created_at, processed_at, type, payload, outbox_status, saga_status, order_status, version)
	VALUES (uuid_generate_v4(), uuid_generate_v4(), now(), now(),'OrderProcessingSaga', '{
  "id": "31ed1839-6105-4c23-b070-74ae095b8371",
  "price": 1,
  "sagaId": "95ff66c2-d58e-4a5b-a0c6-92f88fc3a556",
  "orderId": "632a7ff9-f239-4cc6-bed3-adadffadc6c0",
  "products": [
    {
      "id": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
      "quantity": 1
    }
  ],
  "createdAt": "2023-06-14T23:40:50.524964Z",
  "restaurantId": "d215b5f8-0249-4dc5-89a3-51fd148cfb45",
  "restaurantOrderStatus": "PAID"
}', 'STARTED', 'STARTED', 'PENDING', 0);



INSERT INTO payment.order_outbox(
	id, saga_id, created_at, processed_at, type, payload, outbox_status, payment_status, version)
	VALUES (uuid_generate_v4(), uuid_generate_v4(), now(), now(), 'OrderProcessingSaga', '{
  "price": 1,
  "orderId": "632a7ff9-f239-4cc6-bed3-adadffadc6c0",
  "createdAt": "2023-06-14T23:40:49.611292Z",
  "paymentId": "2a0d1e29-0584-42c9-9bf0-1d6f358a9703",
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41",
  "paymentStatus": "COMPLETED",
  "failureMessages": []
}', 'STARTED', 'COMPLETED', 0);



INSERT INTO restaurant.order_outbox(
	id, saga_id, created_at, processed_at, type, payload, outbox_status, approval_status, version)
	VALUES (uuid_generate_v4(), uuid_generate_v4(), now(), now(), 'OrderProcessingSaga', '{
  "orderId": "632a7ff9-f239-4cc6-bed3-adadffadc6c0",
  "createdAt": "2023-06-14T23:40:51.427069Z",
  "restaurantId": "d215b5f8-0249-4dc5-89a3-51fd148cfb45",
  "failureMessages": [],
  "orderApprovalStatus": "APPROVED"
}', 'STARTED', 'APPROVED', 0);
