import React from "react";

type InquiryDetailProps = {
  title: string;
  content: string;
  inquiryStatus: "PENDING" | "ANSWERED";
  onStatusChange: () => void; // 콜백 함수 타입 정의
};

const InquiryDetail: React.FC<InquiryDetailProps> = ({
                                                       title,
                                                       content,
                                                       inquiryStatus,
                                                       onStatusChange,
                                                     }) => {
  return (
      <div className="max-w-4xl mx-auto p-4 bg-white border rounded-lg shadow-md">
        <h2 className="text-xl font-bold mb-4">{title}</h2>
        <p className="mb-4">{content}</p>
        <div className="flex justify-between items-center">
        <span
            className={`text-sm ${
                inquiryStatus === "PENDING" ? "text-yellow-500" : "text-green-500"
            }`}
        >
          {inquiryStatus === "PENDING" ? "대기 중" : "답변 완료"}
        </span>
          <button
              onClick={onStatusChange}
              className="px-4 py-2 text-white bg-blue-500 rounded-md hover:bg-blue-600"
          >
            상태 변경
          </button>
        </div>
      </div>
  );
};

export default InquiryDetail;
