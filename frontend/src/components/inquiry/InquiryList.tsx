import React from "react";

type Inquiry = {
  id: number;
  title: string;
  inquiryStatus: "PENDING" | "ANSWERED";
};

type InquiryListProps = {
  inquiries: Inquiry[];
  onInquirySelect: (id: number) => void;
};

const InquiryList: React.FC<InquiryListProps> = ({ inquiries, onInquirySelect }) => {
  return (
      <div className="max-w-4xl mx-auto">
        <div className="mb-4">
          <input
              type="text"
              placeholder="문의 검색..."
              className="w-full p-2 border border-gray-300 rounded-md"
          />
        </div>
        <ul className="space-y-4">
          {inquiries.map((inquiry) => (
              <li
                  key={inquiry.id}
                  className="p-4 bg-white border rounded-lg shadow-md cursor-pointer hover:bg-gray-100"
                  onClick={() => onInquirySelect(inquiry.id)}
              >
                <div className="flex justify-between items-center">
                  <span className="font-bold">{inquiry.title}</span>
                  <span
                      className={`text-sm ${
                          inquiry.inquiryStatus === "PENDING"
                              ? "text-yellow-500"
                              : "text-green-500"
                      }`}
                  >
                {inquiry.inquiryStatus === "PENDING" ? "대기 중" : "답변 완료"}
              </span>
                </div>
              </li>
          ))}
        </ul>
      </div>
  );
};

export default InquiryList;
